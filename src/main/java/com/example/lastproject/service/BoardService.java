//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.service;

import com.example.lastproject.exception.verifyCodeException;
import com.example.lastproject.model.dto.boardWrapper;
import com.example.lastproject.model.dto.boardsWrapper;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.BoardImg;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.model.entity.User;
import com.example.lastproject.repository.BoardImgRepository;
import com.example.lastproject.repository.BoardRepository;
import com.example.lastproject.repository.ReplyRepository;
import com.example.lastproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;

    @Value("${upload.basedir}")
    String baseDir;
    @Value("${upload.server}")
    String uploadServer;
    @Autowired
    BoardImgRepository boardImgRepository;

    @Autowired
    ReplyRepository replyRepository;
     /**게시글 작성*/
    @Transactional
    public boolean createBoard(String principal, BoardRequest req) throws IOException {
        User user = userRepository.findByEmail(principal);
        Board found = new Board();
        found.setDescription(req.getDescription());
        found.setBoardRoles(req.getBoardRoles());
        found.setCategory(req.getCategory());
        found.setPostTitle(req.getPostTitle());
        found.setWriter(user);
        ;
        var saved = boardRepository.save(found);
        log.warn("=========",saved.getId().toString());
        if (req.getArraches() != null && saved !=null) {
            String emailEncoded = new String(Base64.getEncoder().encode(principal.getBytes()));
            for (MultipartFile multi : req.getArraches()) { // 하나씩 반복문 돌면서
                BoardImg boardImg = new BoardImg();
                // 어디다가 file 옮겨둘껀지 File 객체를 정의하고
                File saveDir = new File(baseDir + "/feed/" + saved.getId());
                saveDir.mkdirs();

                String filename = System.currentTimeMillis()
                        + multi.getOriginalFilename().substring(multi.getOriginalFilename().lastIndexOf("."));
                File dest = new File(saveDir, filename);
                multi.transferTo(dest); // 옮기는거 진행
                // 업로드가 끝나면 DB에 기록
                boardImg.setType(multi.getContentType());
                boardImg.setMediaUrl(uploadServer + "/resource/feed/" + saved.getId() + "/" + filename); // 업로드를 한
                // 곳이
                // 어디냐에 따라서 // 결정되는 값
                boardImg.setBoardId(saved);
                boardImgRepository.save(boardImg);
            }
        }
        return true;
    }

    /**게시글 수정*/
    @Transactional
    public boolean boardArraches(String principal, boardArrachesRequest req, passwordRequest pass) throws IOException {
        var user = userRepository.findByEmail(principal);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(pass.getPassword(), user.getPassword())) {
            Board found = new Board();
            found.setBoardDate(LocalDateTime.now());
            found.setId(req.getBoardId().getId());
            found.setDescription(req.getDescription());
            found.setCategory(req.getCategory());
            found.setPostTitle(req.getPostTitle());
            found.setWriter(req.getWriter());
            var saved = boardRepository.save(found);
            if (req.getArraches() != null) {
                boardImgRepository.deleteAllByboardId(req.getBoardId());
                String emailEncoded = new String(Base64.getEncoder().encode(principal.getBytes()));
                for (MultipartFile multi : req.getArraches()) { // 하나씩 반복문 돌면서
                    BoardImg boardImg = new BoardImg();
                    // 어디다가 file 옮겨둘껀지 File 객체를 정의하고
                    File saveDir = new File(baseDir + "/feed/" + saved.getId());
                    saveDir.mkdirs();

                    String filename = System.currentTimeMillis()
                            + multi.getOriginalFilename().substring(multi.getOriginalFilename().lastIndexOf("."));
                    File dest = new File(saveDir, filename);
                    multi.transferTo(dest); // 옮기는거 진행
                    // 업로드가 끝나면 DB에 기록
                    boardImg.setType(multi.getContentType());
                    boardImg.setMediaUrl(uploadServer + "/resource/feed/" + saved.getId() + "/" + filename); // 업로드를 한
                    // 곳이
                    // 어디냐에 따라서 // 결정되는 값
//                    boardImg.setId();
                    boardImg.setBoardId(saved);
                    boardImgRepository.save(boardImg);
                }

            }
            return true;
        }
        return true;
    }

    /**게시글 삭제*/
    @Transactional
    public void boardDeleteHandle(String principal, passwordRequest pass,boardIdRequest req) {
      var user = userRepository.findByEmail(principal);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(pass.getPassword(), user.getPassword())) {
        replyRepository.deleteAllByBoardId(req.getBoardId());
        boardImgRepository.deleteAllByboardId(req.getBoardId());

        boardRepository.delete(req.getBoardId());
        }

    }

    /**전체글 갯수*/
    @Transactional
    public long totalCount() {
        return boardRepository.count();
    }

    /**전체 글 가져오기*/
    @Transactional
    public List<boardsWrapper> pageload(int page) {
       List<Board> boardList = boardRepository.findAll(PageRequest.of(page-1,5)).toList();

       return boardList.stream().map(board -> new boardsWrapper(board)).toList();
    }

    /**특정카테고리 전체 가져오기*/
    @Transactional
    public List<boardsWrapper> readCategoryBoard(categoryRequest req, int page) {
        if(req.getCategory() != null){
            List<Board> boardList =  boardRepository.findAllByCategory(req.getCategory());
            return boardList.stream().map(board -> new boardsWrapper(board)).toList();
        }else{
            List<Board> boardList = boardRepository.findAll(PageRequest.of(page-1,5)).toList();

            return boardList.stream().map(board -> new boardsWrapper(board)).toList();
        }

    }
    /**특정 카테고리 글 갯수*/
    @Transactional
    public long categoryTotalCount(categoryRequest req) {
        return boardRepository.countByCategory(req.getCategory());
    }

    /**특정권한 유저통과시키는 서비스*/
    @Transactional
    public List<boardWrapper> detailPage(String principal, boardIdRequest req, List<Reply> replts) throws verifyCodeException {
        User user = userRepository.findByEmail(principal);
        log.info("boardId = {}"+req);
        Optional<Board> board = boardRepository.findById(req.getBoardId().getId());
        log.warn(board.get().getBoardRoles());
        board.get().setReplyList(replts);
        if (board.isPresent() && board.get().getBoardRoles().contains("basic")) {
            // basic 권한을 가진 게시판은 모든 사용자가 볼 수 있음
            return board.stream().map(boards -> new boardWrapper(boards)).toList();
        } else if (board.isPresent() && board.get().getBoardRoles().contains("middle")) {
            // middle 권한을 가진 게시판은 master 권한을 가진 유저 또는 middle 권한을 가진 유저만 볼 수 있음
            if (user.getRoles().contains("master") || user.getRoles().contains("middle")) {
                return board.stream().map(boards -> new boardWrapper(boards)).toList();
            } else {
                throw new verifyCodeException("게시글을 볼 수 있는 권한이 없습니다.");
            }
        } else if (board.isPresent() && board.get().getBoardRoles().contains("master")) {
            // master 권한을 가진 게시판은 master 권한을 가진 유저만 볼 수 있음
            if (user.getRoles().contains("master")) {
                return board.stream().map(boards -> new boardWrapper(boards)).toList();
            } else {
                throw new verifyCodeException("게시글을 볼 수 있는 권한이 없습니다.");
            }
        } else {
            throw new verifyCodeException("게시글을 볼 수 있는 권한이 없습니다.");
        }
    }

}

