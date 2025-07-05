package com.sil.jmongo.domain.attachment.entity;

import com.sil.jmongo.global.entity.Base;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 게시판
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "attachment")
public class Attachment extends Base {
    @Id
    private String id;
    private String uploadPath;  // 업로드경로
    private String orgFileName; // 원본파일명
    private String sysFileName; // 시스템파일명
    private String parentType;  // 파일대상 게시판
    private String parentId;    // 파일대상 게시판 아이디
}
