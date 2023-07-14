package com.example.lastproject.service;

import com.example.lastproject.exception.ReplyUserdException;
import com.example.lastproject.model.dto.request.boardIdRequest;
import com.example.lastproject.model.dto.request.replyDeleteRequest;
import com.example.lastproject.model.dto.request.replyPacthRequest;
import com.example.lastproject.model.dto.request.replyRequest;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.repository.BoardRepository;
import com.example.lastproject.repository.ReplyRepository;
import com.example.lastproject.repository.UserRepository;
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
    /**댓글작성 서비스*/
    public void replyCreate(boardIdRequest req, String principal, replyRequest replyReq) throws ReplyUserdException {
        var user = userRepository.findByEmail(principal);
        log.warn("boasad=",String.valueOf(req.getBoardId()));
        var boar = boardRepository.findById(req.getBoardId()).orElseThrow(() -> new ReplyUserdException("삭제된 게시판입니다."));
        if(user != null && req != null){
            log.warn(String.valueOf(req.getBoardId()));
            Reply replys = new Reply();
            replys.setComment(replyReq.getComment());
            replys.setUserId(user);
            replys.setBoardId(boar);
            replyRepository.save(replys);
        }


    }


    public List<Reply> findbyReply(boardIdRequest req) {
        return replyRepository.findByBoardId(req.getBoardId());
    }

    /**댓글삭제*/
    public void replyDelete(String principal, replyDeleteRequest req) throws ReplyUserdException {
        var user = userRepository.findByEmail(principal);
        log.warn("user =" +user.getId());
        var reply = replyRepository.findById(req.getReplyId());
        log.warn("reply =" +reply.get().getUserId().getId());
        if(!user.getId().equals(reply.get().getUserId().getId())){
            throw new ReplyUserdException("댓글 작성자 또는 운영자만 삭제 할수있습니다.");
        }

        replyRepository.deleteById(reply.get().getId());

    }

    /**댓글 수정*/
    public void replyPatch(String principal, replyPacthRequest req) throws ReplyUserdException {
        var email = userRepository.findByEmail(principal);
        var reply =replyRepository.findById(req.getId());
        if(email.getId() != reply.get().getUserId().getId()){
            throw new ReplyUserdException("댓글 작성자만이 수정할수있습니다.");
        }else{
            Reply replys = new Reply();
            replys.setId(req.getId());
            replys.setUserId(email);
            replys.setBoardId(reply.get().getBoardId());
            replys.setComment(req.getComment());
            replyRepository.save(replys);
        }

    }
}
