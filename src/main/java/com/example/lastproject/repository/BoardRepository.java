//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.repository;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    List<Board> findAllByCategory(String req);

    long countByCategory(String req);

    Optional<Board> findById(Integer boardId);

    List<Board> findByWriter(User Writer);

    void deleteById(Board boards);

    Optional<Board> findById(Board boardId);
}
