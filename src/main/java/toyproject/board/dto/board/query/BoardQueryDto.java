package toyproject.board.dto.board.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class BoardQueryDto {

    private Long boardId;
    private String title;
    private String content;
    private String nickname;
    private Long memberId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @Builder
    @QueryProjection
    public BoardQueryDto(Long boardId, String title, String content, String nickname, Long memberId, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.memberId = memberId;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

}
