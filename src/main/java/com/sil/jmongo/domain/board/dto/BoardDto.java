package com.sil.jmongo.domain.board.dto;

import com.sil.jmongo.domain.board.entity.Board;
import com.sil.jmongo.global.code.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class BoardDto {
    /**
     * 등록
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {

        @Schema(description = "제목")
        @NotBlank @Size(max = 200)
        private String title;    // 제목

        @Schema(description = "내용")
        @NotBlank
        private String content;    // 내용

        @Schema(description = "게시판유형", example = "NORMAL")
        private BoardType boardType = BoardType.NORMAL;        // 롤

        public Board toEntity() {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .boardType(boardType.name())
                    .build();
        }
    }

    /**
     * 수정
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyRequest {

        @Schema(description = "제목")
        @NotBlank @Size(max = 200)
        private String title;    // 제목

        @Schema(description = "내용")
        @NotBlank
        private String content;    // 내용

        public void modifyBoard(Board board) {
            board.setTitle(this.title);
            board.setContent(this.content);
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String boardType;   // 게시판유형
        private String title;       // 게시글제목
        private String content;     // 게시글내용
        private boolean enabled; // 활성화여부

        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;

        public static BoardDto.Response toDto(Board board) {
            return BoardDto.Response.builder()
                    .id(board.getId())
                    .boardType(board.getBoardType())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .enabled(board.isEnabled())
                    .createdBy(board.getCreatedBy())
                    .createdAt(board.getCreatedAt())
                    .modifiedBy(board.getModifiedBy())
                    .modifiedAt(board.getModifiedAt())
                    .enabled(board.isEnabled())
                    .build();
        }
    }


    /**
     * 조회조건
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {

        @Schema(description = "제목, 내용 중 하나")
        private String keyword;

        @Schema(description = "등록일 시작 (yyyyMMdd)", example = "20250101")
        private String fromDate;

        @Schema(description = "등록일 종료 (yyyyMMdd)", example = "20301231")
        private String toDate;

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
        private int page = 0;

        @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
        private int size = 10;

        @Schema(description = "정렬 기준 필드", example = "createdAt", defaultValue = "createdAt")
        private String sortBy = "createdAt";

        @Schema(description = "내림차순 여부", example = "true", defaultValue = "true")
        private boolean desc = true;
    }
}
