package toyproject.board.dto.board.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import toyproject.board.dto.comment.query.CommentQueryDto;

@Getter
@Setter
public class BoardDetail {

    private BoardQueryDto board;
    private Page<CommentQueryDto> comments;

    @Builder
    public BoardDetail(BoardQueryDto board, Page<CommentQueryDto> comments) {
        this.board = board;
        this.comments = comments;
    }
}
