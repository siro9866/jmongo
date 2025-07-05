package com.sil.jmongo.domain.attachment.dto;

import com.sil.jmongo.domain.attachment.entity.Attachment;
import com.sil.jmongo.global.code.ParentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class AttachmentDto {

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

        public Attachment toEntity() {
            return Attachment.builder()
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

        public static Response toDto(Attachment attachment) {
            return Response.builder()
                    .id(attachment.getId())
                    .uploadPath(attachment.getUploadPath())
                    .orgFileName(attachment.getOrgFileName())
                    .sysFileName(attachment.getSysFileName())
                    .parentType(attachment.getParentType())
                    .parentId(attachment.getParentId())
                    .createdBy(attachment.getCreatedBy())
                    .createdAt(attachment.getCreatedAt())
                    .modifiedBy(attachment.getModifiedBy())
                    .modifiedAt(attachment.getModifiedAt())
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
