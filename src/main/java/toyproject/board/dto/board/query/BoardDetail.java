package toyproject.board.dto.board.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.dto.comment.query.CommentQueryDto;

import java.util.List;

@Getter
@Setter
public class BoardDetail {

    private BoardQueryDto board;
    private List<CommentQueryDto> comments;

    @Builder
    public BoardDetail(BoardQueryDto board, List<CommentQueryDto> comments) {
        this.board = board;
        this.comments = comments;
    }
}
