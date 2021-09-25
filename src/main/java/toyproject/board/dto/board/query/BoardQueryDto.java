package toyproject.board.dto.board.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 게시물 상세 조회 DTO (댓글 X, 비밀번호 X)
@NoArgsConstructor
@Getter
@Setter
public class BoardQueryDto {

    private Long boardId;
    private String title;
    private String content;
    private Integer views;
    private String nickname;
    private Long memberId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @Builder
    public BoardQueryDto(Long boardId, String title, String content, Integer views, String nickname, Long memberId, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.views = views;
        this.nickname = nickname;
        this.memberId = memberId;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
