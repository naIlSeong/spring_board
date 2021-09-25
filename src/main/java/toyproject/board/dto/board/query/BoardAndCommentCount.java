package toyproject.board.dto.board.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 게시물 리시트 조회 DTO
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardAndCommentCount {

    private Long boardId;
    private Integer views;
    private String title;
    private String nickname;
    private Long memberId;
    private Long commentCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @QueryProjection
    public BoardAndCommentCount(Long boardId, Integer views, String title, String nickname, Long memberId, Long commentCount, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.boardId = boardId;
        this.views = views;
        this.title = title;
        this.nickname = nickname;
        this.memberId = memberId;
        this.commentCount = commentCount;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
