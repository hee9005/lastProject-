package com.example.lastproject.service;

import com.example.lastproject.exception.NotBoardException;
import com.example.lastproject.exception.ReplyUserdException;
import com.example.lastproject.exception.verifyCodeException;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.model.entity.User;
import com.example.lastproject.repository.BoardRepository;
import com.example.lastproject.repository.NestedrReplyRepository;
import com.example.lastproject.repository.ReplyRepository;
import com.example.lastproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReplyService {

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NestedrReplyRepository nestedrReplyRepository;

    /**댓글작성 서비스*/
    @Transactional
    public void replyCreate(boardIdRequest req, String principal, replyRequest replyReq) throws ReplyUserdException, NotBoardException {
        var user = userRepository.findByEmail(principal);
        log.warn("boasad=",String.valueOf(req.getBoardId()));
        var boar = boardRepository.findById(req.getBoardId().getId()).orElseThrow(() -> new NotBoardException("삭제된 게시판입니다."));
        if(user != null && req != null){
            log.warn(String.valueOf(req.getBoardId()));
            Reply replys = new Reply();
            replys.setComment(replyReq.getComment());
            replys.setUser(user);
            replys.setBoard(boar);
            replyRepository.save(replys);
        }


    }

    @Transactional
    public List<Reply> findbyReply(boardIdRequest req) {
        return replyRepository.findByBoardId(req.getBoardId().getId());
    }


    /**댓글삭제*/
    @Transactional
    public void replyDelete(String principal, replyDeleteRequest req) throws ReplyUserdException {
        var user = userRepository.findByEmail(principal);
        var reply = replyRepository.findById(req.getReplyId());
        if(!user.getRoles().contains("master") || !user.getRoles().contains("admin") ){
        if(!user.getId().equals(reply.get().getUser().getId())){
            throw new ReplyUserdException("댓글 작성자 또는 운영자만 삭제 할수있습니다.");
        }}
        nestedrReplyRepository.deleteAllByreplyId(reply);
        replyRepository.deleteById(reply.get().getId());

    }

    /**댓글 수정*/
    @Transactional
    public void replyPatch(String principal, replyPacthRequest req) throws ReplyUserdException {
        var email = userRepository.findByEmail(principal);
        var reply =replyRepository.findById(req.getId());
        if(email.getId() != reply.get().getUser().getId()){
            throw new ReplyUserdException("댓글 작성자만이 수정할수있습니다.");
        }else{
            Reply replys = new Reply();
            replys.setId(req.getId());
            replys.setUser(email);
            replys.setBoard(reply.get().getBoard());
            replys.setComment(req.getComment());
            replyRepository.save(replys);
        }

    }

    public long replyCount(UserIdRequest req) throws verifyCodeException {
        User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new verifyCodeException("등록이 안된 유저입니다."));
        log.warn("userId ={}",user.getId());
        return replyRepository.countByUserId(user.getId());
    }


}
