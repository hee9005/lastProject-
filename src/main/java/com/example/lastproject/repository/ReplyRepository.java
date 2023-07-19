package com.example.lastproject.repository;

import com.example.lastproject.model.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Integer> {

    List<Reply> findByBoardId(Integer req);

    void deleteById(Integer id);


    void deleteAllByBoardId(Integer id);

    long countByUserId(Integer user);

    List<Reply> findByUserId(Integer user);
}
