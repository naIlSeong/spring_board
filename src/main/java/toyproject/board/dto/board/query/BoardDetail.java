package toyproject.board.dto.board.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.dto.board.BoardNoPw;
import toyproject.board.dto.comment.CommentNoPw;

import java.util.List;

@Getter
@Setter
public class BoardDetail {

    private BoardNoPw board;
    private List<CommentNoPw> comments;

    @Builder
    public BoardDetail(BoardNoPw board, List<CommentNoPw> comments) {
        this.board = board;
        this.comments = comments;
    }
}
