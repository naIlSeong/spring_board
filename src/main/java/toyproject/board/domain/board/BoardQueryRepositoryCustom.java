package toyproject.board.domain.board;

import toyproject.board.dto.board.BoardNoPw;

public interface BoardQueryRepositoryCustom {

    BoardNoPw findNoPasswordById(Long boardId);

}
