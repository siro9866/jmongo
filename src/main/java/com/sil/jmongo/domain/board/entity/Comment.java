package com.sil.jmongo.domain.board.entity;

import com.sil.jmongo.global.entity.Base;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 댓글
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comment")
public class Comment extends Base {
    @Id
    private String id;
    private String boardId;     // 게시판 아이디
    private String commentary;  // 댓글
    @Builder.Default
    private boolean enabled = true; // 활성화여부
}
