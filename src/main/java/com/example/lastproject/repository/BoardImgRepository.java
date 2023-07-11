package com.example.lastproject.repository;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.BoardImg;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardImgRepository extends JpaRepository<BoardImg,Integer> {

    void deleteAllByboardId(Board boardId);
}
