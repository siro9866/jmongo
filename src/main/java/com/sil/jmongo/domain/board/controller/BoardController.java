package com.sil.jmongo.domain.board.controller;

import com.sil.jmongo.domain.board.dto.BoardDto;
import com.sil.jmongo.domain.board.service.BoardService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "게시판", description = "게시판 API")
public class BoardController {

    private final BoardService boardService;
    private final UtilMessage utilMessage;

    @Operation(summary = "게시판목록", description = "게시판목록")
    @GetMapping
    public ResponseEntity<Page<BoardDto.Response>> listBoard(@ParameterObject @ModelAttribute BoardDto.Search search) {
        Page<BoardDto.Response> boards = boardService.boardList(search);
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    @Operation(summary = "게시판상세", description = "게시판상세")
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto.Response> detailBoard(@PathVariable String id) {
        BoardDto.Response board = boardService.boardDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(board);
    }

    @Operation(summary = "게시판등록", description = "게시판등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoardDto.Response>> createBoard(
            @ParameterObject @RequestPart @ModelAttribute @Valid BoardDto.CreateRequest request,
            @RequestPart(name = "mFiles", required = false) MultipartFile[] mFiles
    ) throws IOException {
        BoardDto.Response board = boardService.boardCreate(request, mFiles);

        ApiResponse<BoardDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.create", null), board);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "게시판수정", description = "게시판수정")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoardDto.Response>> boardModify(@PathVariable String id
        , @ParameterObject @ModelAttribute @Valid BoardDto.ModifyRequest request
        , @RequestPart(name = "files", required = false) MultipartFile[] files) throws IOException {
        BoardDto.Response board = boardService.boardModify(id, request, files);

        ApiResponse<BoardDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.modify", null), board);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "게시판삭제", description = "게시판삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardDto.Response>> deleteBoard(@PathVariable String id) throws IOException {
        boardService.boardDelete(id);
        ApiResponse<BoardDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.delete", null), null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}