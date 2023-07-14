//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.service;

import com.example.lastproject.exception.AlreadyVerifedException;
import com.example.lastproject.exception.ExistUserEmailException;
import com.example.lastproject.exception.NotExistCodeException;
import com.example.lastproject.exception.verifyCodeException;
import com.example.lastproject.model.dto.request.emailCertifed;
import com.example.lastproject.model.dto.request.emaliEequest;
import com.example.lastproject.model.dto.request.joinRequest;
import com.example.lastproject.model.dto.request.loginRequest;
import com.example.lastproject.model.dto.request.passwordRequest;
import com.example.lastproject.model.dto.response.VerificationCodeResponseDate;
import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.User;
import com.example.lastproject.model.entity.VerificationCode;
import com.example.lastproject.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Random;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**인증 코드 발급*/
    @Transactional
    public VerificationCodeResponseDate createCode(emaliEequest req) throws MessagingException, AlreadyVerifedException {
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
    public void emailCertifed(emailCertifed req) throws NotExistCodeException, verifyCodeException {
        Optional<VerificationCode> result =verificationCodeRepository.findTop1ByEmailOrderByCreatedDesc(req.getEmail());
        VerificationCode found = (VerificationCode)result.orElseThrow(() -> {
            return new NotExistCodeException();
        });
        long elapsed = System.currentTimeMillis() - found.getCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (elapsed > 600000L) {
            throw new verifyCodeException("인증코드 유효시간이 만료되었습니다.");
        } else if (!found.getCode().equals(req.getCode())) {
            throw new verifyCodeException("인증코드가 일치하지 않습니다.");
        } else {
            found.setState("Y");
           verificationCodeRepository.save(found);
        }
    }
    /**회원가입*/
    @Transactional
    public void createUser(joinRequest req) throws AlreadyVerifedException, ExistUserEmailException, verifyCodeException {
        if (this.userRepository.existsByEmail(req.getEmail())) {
            throw new ExistUserEmailException();
        } else {
            VerificationCode found = (VerificationCode)this.verificationCodeRepository.findTop1ByEmailOrderByCreatedDesc(req.getEmail()).orElseThrow(() -> {
                return new verifyCodeException("인증코드 검증 기록이 존재하지않습니다..");
            });
            if (found.getState().equals("N")) {
                throw new verifyCodeException("아직 미 인증 상태입니다.");
            } else {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                User forun = new User();
                forun.setPassword(passwordEncoder.encode(req.getPassword()));
                forun.setName(req.getName());
                forun.setEmail(req.getEmail());
                forun.setRoles("basic");
              userRepository.save(forun);
            }
        }
    }

    /**로그인*/
    @Transactional
    public void loginHandle(loginRequest req) throws verifyCodeException {
        log.warn(req.getEmail());
        User user =userRepository.findByEmail(req.getEmail());
        log.warn(user.getPassword());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new verifyCodeException("비밀번호가 잘못 입력되었습니다.");
        }
    }

    /**유저 삭제(후에고쳐야함)*/
    @Transactional
    public void userDelete(String email, passwordRequest req) throws verifyCodeException {
        User user = userRepository.findByEmail(email);
        log.warn("userEmail" + user.getEmail());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(req.getPassword(), user.getPassword())) {
           var board=boardRepository.findByWriter(user);
            for (Board boards : board) {
                replyRepository.deleteAllByBoardId(boards);
                boardImgRepository.deleteAllByboardId(boards);
                boardRepository.deleteById(boards.getId());
            }
          userRepository.deleteByEmail(user.getEmail());
          verificationCodeRepository.deleteByEmail(user.getEmail());
        } else {
            throw new verifyCodeException("비밀번호가 잘못 입력되었습니다.");
        }
    }
}
