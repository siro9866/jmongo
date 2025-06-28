package com.sil.jmongo.domain.board.service;

import com.sil.jmongo.domain.board.dto.CommentDto;
import com.sil.jmongo.domain.board.entity.Comment;
import com.sil.jmongo.domain.board.repository.BoardRepository;
import com.sil.jmongo.domain.board.repository.CommentRepository;
import com.sil.jmongo.global.exception.CustomException;
import com.sil.jmongo.global.response.ResponseCode;
import com.sil.jmongo.global.util.UtilCommon;
import com.sil.jmongo.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final MongoTemplate mongoTemplate;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UtilMessage utilMessage;

    /**
     * 목록
     * @param search
     * @return
     */
    public Page<CommentDto.Response> listComment(CommentDto.Search search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Comment> commentPage = commentRepository.findByBoardIdAndEnabledTrue(search.getBoardId(), pageable);
        return commentPage.map(CommentDto.Response::toDto);

    }

    /**
     * 등록
     * @param request
     * @return
     */
    public CommentDto.Response createComment(CommentDto.CreateRequest request) {

        boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        Comment savedComment = commentRepository.save(request.toEntity());
        return CommentDto.Response.toDto(savedComment);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    public void modifyComment(String id, CommentDto.ModifyRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        // 권한확인(자기것만)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getName().equals(comment.getCreatedBy())){
            throw new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("comment.auth.forbidden", null));
        }

        request.modifyComment(comment);

        // MongoDB에 명시적으로 저장
        commentRepository.save(comment);
    }

    /**
     * 삭제
     * @param id
     */
    public void deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        // 권한확인(자기것만)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getName().equals(comment.getCreatedBy())){
            throw new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("comment.auth.forbidden", null));
        }

        commentRepository.deleteById(comment.getId());
    }
}