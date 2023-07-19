package com.example.lastproject.service;

import com.example.lastproject.exception.ReplyUserdException;
import com.example.lastproject.model.dto.request.NestedrReplyBoardId;
import com.example.lastproject.model.dto.request.NestedrReplyModifyRequest;
import com.example.lastproject.model.dto.request.NestedrReplyRequest;
import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.NestedrReply;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class NestedrReplyService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    NestedrReplyRepository nestedrReplyRepository;

    @Autowired
    ReplyRepository replyRepository;

    /**대댓글 작성 서비스*/
    @Transactional
    public void nestedrReplyCreate(String principal, NestedrReplyRequest req, NestedrReplyBoardId nrb) throws ReplyUserdException {
    User user = userRepository.findByEmail(principal);
    Reply reply =replyRepository.findById(nrb.getReplyId()).orElseThrow(() -> new ReplyUserdException("삭제된 댓글입니다.."));
    log.warn(reply.getId().toString());
        NestedrReply found = new NestedrReply();
        found.setNestedrReplyContent(req.getNestedrReplyContent());
        found.setUserId(user);
        found.setReplyId(reply);
        nestedrReplyRepository.save(found);
    }

    /**대댓글 수정 서비스*/
    @Transactional
    public void modifyHandle(String principal, NestedrReplyModifyRequest req, NestedrReplyRequest nrb) throws ReplyUserdException {
     User user = userRepository.findByEmail(principal);
       NestedrReply reply = nestedrReplyRepository.findById(req.getNestedrReply()).orElseThrow(() -> new ReplyUserdException("수정할려는 글을 찾을수 없습니다."));
    if(!user.getId().equals(reply.getUserId().getId())){
        throw new ReplyUserdException("댓글을 작성한 유저만 수정할수있습니다.");
    }
        NestedrReply found = new NestedrReply();
        found.setNestedrReplyContent(nrb.getNestedrReplyContent());
        found.setUserId(user);
        found.setReplyId(reply.getReplyId());
        found.setId(reply.getId());
        found.setNestedrReplydate(LocalDateTime.now());
        nestedrReplyRepository.save(found);
    }

    /**대댓글 삭제*/
    @Transactional
    public void deleteHandle(NestedrReplyModifyRequest req, String principal) throws ReplyUserdException {
      User user = userRepository.findByEmail(principal);
      NestedrReply reply = nestedrReplyRepository.findById(req.getNestedrReply()).orElseThrow(() -> new ReplyUserdException("삭제할려는 글을 찾을수 없습니다"));
        if(!user.getId().equals(reply.getUserId().getId())){
            throw new ReplyUserdException("댓글을 작성한 유저만 삭제할수있습니다.");
        }
        nestedrReplyRepository.deleteById(reply.getId());
}

    }
