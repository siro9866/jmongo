package com.sil.jmongo.domain.board.repository;

import com.sil.jmongo.domain.board.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findByBoardIdAndEnabledTrue(String boardId, Pageable pageable);
}