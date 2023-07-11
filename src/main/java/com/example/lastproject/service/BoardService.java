//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.service;

import com.example.lastproject.exception.verifyCodeException;
import com.example.lastproject.model.dto.request.BoardRequest;
import com.example.lastproject.model.dto.request.boardArrachesRequest;
import com.example.lastproject.model.dto.request.boardIdRequest;
import com.example.lastproject.model.dto.request.passwordRequest;
import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.BoardImg;
import com.example.lastproject.model.entity.User;
import com.example.lastproject.repository.BoardImgRepository;
import com.example.lastproject.repository.BoardRepository;
import com.example.lastproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

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

    /**
     * 게시글 작성
     */
    @Transactional
    public boolean createBoard(String principal, BoardRequest req) throws IOException {
        User user = userRepository.findByEmail(principal);
        Board found = new Board();
        found.setDescription(req.getDescription());
        found.setCategory(req.getCategory());
        found.setPostTitle(req.getPostTitle());
        found.setWriter(user);
        var saved = boardRepository.save(found);
        if (req.getArraches() != null) {
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

    @Transactional
    public void boardDeleteHandle(String principal, passwordRequest pass,boardIdRequest req) {
      var user = userRepository.findByEmail(principal);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(pass.getPassword(), user.getPassword())) {
        boardImgRepository.deleteAllByboardId(req.getBoardId());

        boardRepository.delete(req.getBoardId());
        }

    }
}

