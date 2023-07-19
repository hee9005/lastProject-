//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.service;

import com.example.lastproject.model.dto.request.modifyMemberInfoRequest;
import com.example.lastproject.exception.*;
import com.example.lastproject.model.dto.ReplyWrapper;
import com.example.lastproject.model.dto.UserWrapper;
import com.example.lastproject.model.dto.boardsWrapper;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.model.dto.response.VerificationCodeResponseDate;
import com.example.lastproject.model.dto.response.passwordRequest;
import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.model.entity.User;
import com.example.lastproject.model.entity.VerificationCode;
import com.example.lastproject.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    VerificationCodeRepository verificationCodeRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    BoardImgRepository boardImgRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    NestedrReplyRepository nestedrReplyRepository;

    @Value("${upload.basedir}")
    String baseDir;

    @Value("${upload.server}")
    String uploadServer;
    /**인증 코드 발급*/
    @Transactional
    public VerificationCodeResponseDate createCode(emaliRequest req) throws MessagingException, AlreadyVerifedException {
        Optional<VerificationCode> found = this.verificationCodeRepository.findTop1ByEmailOrderByCreatedDesc(req.getEmail());
        if (found.isPresent() && !((VerificationCode)found.get()).getState().equals("N")) {
            throw new AlreadyVerifedException("인증이 완료된 이메일입니다.");
        } else {
            Random random = new Random();
            int randNum = random.nextInt(1000000);
            String code = String.format("%06d", randNum);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(req.getEmail());
            helper.setFrom("hee900590@gmail.com");
            helper.setSubject("lastProject");
            helper.setText("<div>\n<h1>이메일 인증코드</h1>\n<p style=\"color:orange\">\nlastProject커뮤니티에 가입하신걸 환영합니다.\n아래의인증코드를 인력하시면가입이 정상적으로 완료됩니다.\n</p>\n<h1>#code</h1>\n</div>\n".replaceAll("#code", code), true);
            javaMailSender.send(message);
            VerificationCode issuance = new VerificationCode(code, req.getEmail(), "N");
            VerificationCode saved = (VerificationCode)verificationCodeRepository.save(issuance);
            return new VerificationCodeResponseDate(saved);
        }
    }

    /**인증코드 확인*/
    @Transactional
    public void emailCertifed(emailCertifed req) throws NotExistCodeException, verifyCodeException, UserPassedException {
        Optional<VerificationCode> result =verificationCodeRepository.findTop1ByEmailOrderByCreatedDesc(req.getEmail());
        VerificationCode found = (VerificationCode)result.orElseThrow(() -> {
            return new NotExistCodeException();
        });
        long elapsed = System.currentTimeMillis() - found.getCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if(found.getState().equals("Y")){
            throw new UserPassedException("인증이 완료된 이메일입니다.");
        }
        if (!found.getCode().equals(req.getCode())) {
            throw new verifyCodeException("인증코드가 일치하지 않습니다.");
        } else if (elapsed > 600000L) {
            throw new verifyCodeException("인증코드 유효시간이 만료되었습니다.");
        } else {
            found.setState("Y");
           verificationCodeRepository.save(found);
        }
    }
    /**회원가입*/
    @Transactional
    public void createUser(joinRequest req) throws AlreadyVerifedException, ExistUserEmailException, verifyCodeException, UnauthorizedException {
        if (this.userRepository.existsByEmail(req.getEmail())) {
            throw new ExistUserEmailException();
        } else {
            VerificationCode found = (VerificationCode)this.verificationCodeRepository.findTop1ByEmailOrderByCreatedDesc(req.getEmail()).orElseThrow(() -> {
                return new verifyCodeException("인증코드 검증 기록이 존재하지않습니다..");
            });
            if (found.getState().equals("N")) {
                throw new UnauthorizedException("아직 미 인증 상태입니다.");
            } else {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                User forun = new User();
                forun.setPassword(passwordEncoder.encode(req.getPassword()));
                forun.setName(req.getName());
                forun.setEmail(req.getEmail());
                forun.setProfilelmage(null);
                forun.setRoles("master");

              userRepository.save(forun);
            }
        }
    }

    /**로그인*/
    @Transactional
    public void loginHandle(loginRequest req) throws verifyCodeException, NotPassWordException {
        log.warn(req.getEmail());
        User user =userRepository.findByEmail(req.getEmail());
        log.warn(user.getPassword());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new NotPassWordException("비밀번호가 잘못 입력되었습니다.");
        }
    }

    /**유저 삭제(후에고쳐야함)*/
    @Transactional
    public void userDelete(String email, passwordRequest req) throws verifyCodeException, NotPassWordException {
        User user = userRepository.findByEmail(email);
        log.warn("userEmail" + user.getEmail());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(req.getPassword(), user.getPassword())) {
           var board=boardRepository.findByWriter(user);
            for (Board boards : board) {
               var reply = replyRepository.findByBoardId(boards.getId());
                for (Reply replys : reply) {
                    nestedrReplyRepository.deleteAllByreplyId(Optional.ofNullable(replys));
                }
                replyRepository.deleteAllByBoardId(boards.getId());
                boardImgRepository.deleteAllByboardId(boards);
                boardRepository.deleteById(boards.getId());
            }
          userRepository.deleteByEmail(user.getEmail());
          verificationCodeRepository.deleteByEmail(user.getEmail());
        } else {
            throw new NotPassWordException("비밀번호가 잘못 입력되었습니다.");
        }
    }

    /**회원 정보 수정*/
    @Transactional
    public UserWrapper modifyMemberInfo(String principal, modifyMemberInfoRequest req) throws IOException {
        User user = userRepository.findByEmail(principal);
        if (req.getName() != null && req.getProfilelmage() != null) {
            User forun = new User();
            forun.setName(req.getName());
            forun.setRoles(user.getRoles());
            forun.setId(user.getId());
            forun.setEmail(user.getEmail());
            forun.setPassword(user.getPassword());
            forun.setJoinDate(user.getJoinDate());
            forun.setSocial(user.getSocial());
            MultipartFile file = req.getProfilelmage();
            File saveDir = new File(baseDir + "/feed/" + user.getId());
            saveDir.mkdirs();

            String filename = System.currentTimeMillis()
                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            File dest = new File(saveDir, filename);
            file.transferTo(dest); // 옮기는거 진행
            // 업로드가 끝나면 DB에 기록
            forun.setProfilelmage(uploadServer + "/resource/feed/" + user.getId() + "/" + filename); // 업로드를 한
            log.warn("img={}",forun.getProfilelmage());
            User saved =  userRepository.save(forun);
            return new UserWrapper(saved);
        } else if(req.getName() != null && req.getProfilelmage() == null) {
            User forun = new User();
            forun.setName(req.getName());
            forun.setRoles(user.getRoles());
            forun.setId(user.getId());
            forun.setEmail(user.getEmail());
            forun.setPassword(user.getPassword());
            forun.setJoinDate(user.getJoinDate());
            forun.setSocial(user.getSocial());
            forun.setProfilelmage(user.getProfilelmage());
            log.warn("img={}",forun.getProfilelmage());
            User saved =  userRepository.save(forun);
            return new UserWrapper(saved);
        }else if(req.getName() == null && req.getProfilelmage() != null){
            User forun = new User();
            forun.setName(user.getName());
            forun.setRoles(user.getRoles());
            forun.setId(user.getId());
            forun.setEmail(user.getEmail());
            forun.setPassword(user.getPassword());
            forun.setJoinDate(user.getJoinDate());
            forun.setSocial(user.getSocial());
            MultipartFile file = req.getProfilelmage();
            File saveDir = new File(baseDir + "/feed/" + user.getId());
            saveDir.mkdirs();

            String filename = System.currentTimeMillis()
                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            File dest = new File(saveDir, filename);
            file.transferTo(dest); // 옮기는거 진행
            // 업로드가 끝나면 DB에 기록
            forun.setProfilelmage(uploadServer + "/resource/feed/" + user.getId() + "/" + filename); // 업로드를 한
            log.warn("img={}",forun.getProfilelmage());
            User saved =  userRepository.save(forun);
            return new UserWrapper(saved);
        }

        return null;
    }

    @Transactional
    public List<boardsWrapper> userPostsPage(UserIdRequest req) throws verifyCodeException, notJoinUserException {
       User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new notJoinUserException("등록이 안된 유저입니다."));
        List<Board> boards = boardRepository.findByWriter(user);
        return boards.stream().map(board -> new boardsWrapper(board)).toList();
    }

    @Transactional
    public List<ReplyWrapper> userReplyPage(UserIdRequest req) throws verifyCodeException {
        User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new verifyCodeException("등록이 안된 유저입니다."));
       log.warn("userId ={}",user.getId());
        List<Reply> boards = replyRepository.findByUserId(user.getId());
        return boards.stream().map(board -> new ReplyWrapper(board)).toList();
    }
}
