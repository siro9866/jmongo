package com.sil.jmongo.domain.board.repository;

import com.sil.jmongo.domain.board.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends MongoRepository<Reply, String> {
    Page<Reply> findByBoardIdAndEnabledTrue(String boardId, Pageable pageable);
}