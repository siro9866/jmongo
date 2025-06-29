package com.sil.jmongo.domain.file.dto;

import com.sil.jmongo.domain.file.entity.File;
import com.sil.jmongo.global.code.ParentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class FileDto {

    /**
     * 등록시 기본데이타
     */
    @Getter
    @Setter
    public static class CreateBaseRequest {
        @NotBlank
        private ParentType parentType = ParentType.BOARD;  // 파일대상 게시판
        @NotBlank
        private String parentId;    // 파일대상 게시판 아이디
    }

    /**
     * 등록
     */
    @Getter
    @Setter
    public static class CreateRequest {
        @NotBlank
        private String uploadPath;  // 업로드경로
        @NotBlank
        private String orgFileName; // 원본파일명
        @NotBlank
        private String sysFileName; // 시스템파일명
        @NotBlank
        private ParentType parentType = ParentType.BOARD;  // 파일대상 게시판
        @NotBlank
        private String parentId;    // 파일대상 게시판 아이디

        public File toEntity() {
            return File.builder()
                    .uploadPath(uploadPath)
                    .orgFileName(orgFileName)
                    .sysFileName(sysFileName)
                    .parentType(parentType.name())
                    .parentId(parentId)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String uploadPath;  // 업로드경로
        private String orgFileName; // 원본파일명
        private String sysFileName; // 시스템파일명
        private String parentType;  // 파일대상 게시판
        private String parentId;    // 파일대상 게시판 아이디

        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;

        public static FileDto.Response toDto(File file) {
            return Response.builder()
                    .id(file.getId())
                    .uploadPath(file.getUploadPath())
                    .orgFileName(file.getOrgFileName())
                    .sysFileName(file.getSysFileName())
                    .parentType(file.getParentType())
                    .parentId(file.getParentId())
                    .createdBy(file.getCreatedBy())
                    .createdAt(file.getCreatedAt())
                    .modifiedBy(file.getModifiedBy())
                    .modifiedAt(file.getModifiedAt())
                    .build();
        }
    }

    /**
     * 삭제시 기본데이타
     */
    @Getter
    @Setter
    public static class DeleteRequest {
        @NotBlank
        private String id;
        @NotBlank
        private ParentType parentType;  // 파일대상 게시판
        private String parentId;    // 파일대상 게시판 아이디
    }

    /**
     * 조회조건
     */
    @Getter
    @Setter
    public static class Search {
        @NotBlank
        private ParentType parentType;  // 파일대상 게시판
        @NotBlank
        private String parentId;    // 파일대상 게시판 아이디
    }
}
