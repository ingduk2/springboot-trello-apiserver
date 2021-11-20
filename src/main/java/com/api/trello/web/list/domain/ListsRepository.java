package com.api.trello.web.list.domain;

import com.api.trello.web.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListsRepository extends JpaRepository<Lists, Long> {

    @Query("SELECT l FROM Lists l where l.board = :board ORDER BY l.idx ASC")
    List<Lists> findAllIndexAsc(@Param("board") Board board);

    @Query("SELECT COALESCE(MAX(l.idx), 0) FROM Lists l WHERE l.board = :board")
//    @Query("SELECT COALESCE(MAX(l.idx), 0) FROM Lists l GROUP BY l.board HAVING l.board = :board")
//    @Query("SELECT COALESCE(MAX(l.idx), 0) FROM Lists l GROUP BY l.board")
    Long findMaxIdxByBoard(@Param("board") Board board);
}
