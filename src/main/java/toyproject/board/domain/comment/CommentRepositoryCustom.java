package toyproject.board.domain.comment;

import toyproject.board.dto.comment.CommentNoPw;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentNoPw> getCommentsByBoardId(Long boardId);

}
