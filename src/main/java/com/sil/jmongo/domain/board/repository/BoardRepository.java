package com.sil.jmongo.domain.board.repository;

import com.sil.jmongo.domain.board.entity.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
}