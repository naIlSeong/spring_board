package toyproject.board.dto.comment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentNoPw {

    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    @QueryProjection
    public CommentNoPw(Long commentId, String content, String nickname, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.commentId = commentId;
        this.content = content;
        this.nickname = nickname;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
