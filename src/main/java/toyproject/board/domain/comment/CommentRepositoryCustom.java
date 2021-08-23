package toyproject.board.domain.comment;

import toyproject.board.dto.comment.query.CommentQueryDto;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentQueryDto> getCommentsByBoardId(Long boardId);

}
