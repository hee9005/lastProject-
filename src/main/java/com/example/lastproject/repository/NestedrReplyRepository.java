package com.example.lastproject.repository;

import com.example.lastproject.model.entity.NestedrReply;
import com.example.lastproject.model.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NestedrReplyRepository extends JpaRepository<NestedrReply,Integer> {
    void deleteAllByreplyId(Optional<Reply> id);
}
