//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.service;

import com.example.lastproject.exception.*;
import com.example.lastproject.model.dto.BoardDetaitWrapper;
import com.example.lastproject.model.dto.boardsWrapper;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.model.dto.response.passwordRequest;
import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.BoardImg;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.model.entity.User;
import com.example.lastproject.repository.*;
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

    @Autowired
    NestedrReplyRepository nestedrReplyRepository;
    /**게시글 작성*/
    @Transactional
    public boolean createBoard(String principal, BoardRequest req) throws IOException, boardsExcpiton {
        User user = userRepository.findByEmail(principal);
        if(req.getBoardRoles()==null || req.getCategory() == null || req.getPostTitle() == null){
            throw new boardsExcpiton("게시글의 제목,카테고리,열람권한은 필수 입력조건입니다.");
        }
        Board found = new Board();
        found.setDescription(req.getDescription());
        found.setBoardRoles(req.getBoardRoles());
        found.setCategory(req.getCategory());
        found.setPostTitle(req.getPostTitle());
        found.setWriter(user);
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
    public boolean boardArraches(String principal, boardArrachesRequest req) throws IOException, NotBoardException, notCreateUserBoard {
        var user = userRepository.findByEmail(principal);
        Board boards =boardRepository.findById(req.getBoardId().getId()).orElseThrow(() -> new NotBoardException("삭제된 게시글입니다."));
        if(user.getId() != boards.getWriter().getId()){
            throw new notCreateUserBoard("게시글을 작성한 사용자만 수정할수있습니다.");
        }
        Board found = new Board();
        if(req.getCategory() == null){
            found.setCategory(boards.getCategory());
        }else{
            found.setCategory(req.getCategory());
        }

        if(req.getBoardRoles() == null){
            found.setBoardRoles(boards.getBoardRoles());
        }else{
            found.setBoardRoles(req.getBoardRoles());
        }

        if(req.getDescription() == null){
            found.setDescription(null);
        }else{
            found.setDescription(req.getDescription());
        }

        if(req.getPostTitle() == null){
            found.setPostTitle(boards.getPostTitle());
        }else{
            found.setPostTitle(req.getPostTitle());
        }
        found.setBoardDate(LocalDateTime.now());
            found.setId(req.getBoardId().getId());
            found.setWriter(user);

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

    /**게시글 삭제*/
    @Transactional
    public void boardDeleteHandle(String principal, passwordRequest pass, boardIdRequest req) throws NotBoardException, notCreateUserBoard {
        var user = userRepository.findByEmail(principal);
       Board board = boardRepository.findById(req.getBoardId().getId()).orElseThrow(() -> new NotBoardException("삭제된 게시글입니다."));
        var reply =replyRepository.findByBoardId(req.getBoardId().getId());
        if(!board.getWriter().equals(user)){
            throw new notCreateUserBoard("게시글을 작성한 사용자만 삭제할수있습니다.");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(pass.getPassword(), user.getPassword())) {
            for (Reply replys : reply) {
                nestedrReplyRepository.deleteAllByreplyId(Optional.ofNullable(replys));
            }
            replyRepository.deleteAllByBoardId(req.getBoardId().getId());
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
    public List<boardsWrapper> readCategoryBoard(categoryRequest req) {
            List<Board> boardList =  boardRepository.findByCategory(req.getCategory());
            return boardList.stream().map(board -> new boardsWrapper(board)).toList();
    }
    /**특정 카테고리 글 갯수*/
    @Transactional
    public long categoryTotalCount(categoryRequest req) {
        return boardRepository.countByCategory(req.getCategory());
    }

    /**
     * 특정권한 유저통과시키는 서비스
     */
    @Transactional
    public List<BoardDetaitWrapper> detailPage(String principal, detailId req) throws verifyCodeException, NoPermissionException {
        User user = userRepository.findByEmail(principal);
        var board = boardRepository.findById(req.getBoardId());
        log.warn("boardId={}",board.get().getBoardRoles());
//        log.warn("==",board.get().getBoardRoles());
        if (board.isPresent() && board.get().getBoardRoles().contains("basic")) {
            // basic 권한을 가진 게시판은 모든 사용자가 볼 수 있음
            return board.stream().map(boards -> new BoardDetaitWrapper(boards)).toList();
        } else if (board.isPresent() && board.get().getBoardRoles().contains("middle")) {
            // middle 권한을 가진 게시판은 master 권한을 가진 유저 또는 middle 권한을 가진 유저만 볼 수 있음
            if (user.getRoles().contains("master") || user.getRoles().contains("middle")) {
                return board.stream().map(boards -> new BoardDetaitWrapper(boards)).toList();
            } else {
                throw new NoPermissionException("게시글을 볼 수 있는 권한이 없습니다.");
            }
        } else if (board.isPresent() && board.get().getBoardRoles().contains("master")) {
            // master 권한을 가진 게시판은 master 권한을 가진 유저만 볼 수 있음
            if (user.getRoles().contains("master")) {
                return board.stream().map(boards -> new BoardDetaitWrapper(boards)).toList();
            } else {
                throw new NoPermissionException("게시글을 볼 수 있는 권한이 없습니다.");
            }
        } else {
            throw new NoPermissionException("게시글을 볼 수 있는 권한이 없습니다.");
        }
    }

    public long boardCount(UserIdRequest req) throws notJoinUserException {
       User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new notJoinUserException("등록이 안된 유저입니다."));
        return boardRepository.countBywriter(user);
    }
}

