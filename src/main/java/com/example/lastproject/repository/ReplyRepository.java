package com.example.lastproject.repository;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Integer> {

    List<Reply> findByBoardId(Board req);

    void deleteById(Integer id);


    void deleteAllByBoardId(Board id);
}
