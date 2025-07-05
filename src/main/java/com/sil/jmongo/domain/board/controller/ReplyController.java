package com.sil.jmongo.domain.board.controller;

import com.sil.jmongo.domain.board.dto.ReplyDto;
import com.sil.jmongo.domain.board.service.ReplyService;
import com.sil.jmongo.global.response.ApiResponse;
import com.sil.jmongo.global.util.UtilMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
@Tag(name = "게시판 댓글", description = "게시판 댓글 API")
public class ReplyController {

    private final ReplyService replyService;
    private final UtilMessage utilMessage;

    @Operation(summary = "댓글 목록", description = "댓글 목록")
    @GetMapping("/board/{boardId}")
    public ResponseEntity<Page<ReplyDto.Response>> replyList(@ParameterObject @ModelAttribute ReplyDto.Search search) {
        Page<ReplyDto.Response> replys = replyService.replyList(search);
        return ResponseEntity.status(HttpStatus.OK).body(replys);
    }

    @Operation(summary = "댓글 등록", description = "댓글 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<ReplyDto.Response>> replyCreate(@ParameterObject @ModelAttribute @Valid ReplyDto.CreateRequest request) {
        ReplyDto.Response reply = replyService.replyCreate(request);

        ApiResponse<ReplyDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.create", null), reply);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReplyDto.Response>> replyModify(@PathVariable String id,
                                                           @ParameterObject @ModelAttribute @Valid ReplyDto.ModifyRequest request) {
        ReplyDto.Response reply = replyService.replyModify(id, request);

        ApiResponse<ReplyDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.modify", null), reply);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ReplyDto.Response>> replyDelete(@PathVariable String id) {
        replyService.replyDelete(id);
        ApiResponse<ReplyDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.delete", null), null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}