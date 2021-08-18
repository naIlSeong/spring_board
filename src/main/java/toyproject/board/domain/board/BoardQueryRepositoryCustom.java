package toyproject.board.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toyproject.board.dto.board.BoardNoPw;
import toyproject.board.dto.board.BoardSearchCondition;
import toyproject.board.dto.board.CheckPasswordDto;

public interface BoardQueryRepositoryCustom {

    BoardNoPw findNoPasswordById(Long boardId);

    Page<BoardNoPw> findAllNoPassword(Long memberId, Pageable pageable);

    Page<BoardNoPw> searchBoard(BoardSearchCondition condition, Pageable pageable);

    CheckPasswordDto findPassword(Long boardId);

}
