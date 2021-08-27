package toyproject.board.dto.board.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardAndCommentCount {

    private Long boardId;
    private String title;
    private String content;
    private String nickname;
    private Long memberId;
    private Long commentCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @QueryProjection
    public BoardAndCommentCount(Long boardId, String title, String content, String nickname, Long memberId, Long commentCount, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.memberId = memberId;
        this.commentCount = commentCount;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
