package toyproject.board.domain.board.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toyproject.board.dto.board.query.BoardAndCommentCount;
import toyproject.board.dto.board.query.BoardQueryDto;
import toyproject.board.dto.board.query.BoardSearchCondition;
import toyproject.board.dto.board.query.CheckPasswordDto;

import java.util.List;

public interface BoardQueryRepositoryCustom {

    BoardQueryDto findNoPasswordById(Long boardId);

    // Page<BoardQueryDto> findAllNoPassword(Long memberId, Pageable pageable);

    // Page<BoardQueryDto> searchBoard(BoardSearchCondition condition, Pageable pageable);

    CheckPasswordDto findPassword(Long boardId);

    Page<BoardAndCommentCount> getBoardList(Pageable pageable);

    Page<BoardAndCommentCount> searchBoard(BoardSearchCondition condition, Pageable pageable);

    List<Long> findAllBoardIdByMemberId(Long memberId);

}