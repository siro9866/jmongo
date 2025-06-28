package com.sil.jmongo.domain.board.entity;

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
@Document(collection = "board")
public class Board extends Base {
    @Id
    private String id;
    private String boardType;   // 게시판유형
    private String title;       // 게시글제목
    private String content;     // 게시글내용
    @Builder.Default
    private boolean enabled = true; // 활성화여부
}
