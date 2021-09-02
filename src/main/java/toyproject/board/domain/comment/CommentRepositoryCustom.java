package toyproject.board.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toyproject.board.dto.board.query.CheckPasswordDto;
import toyproject.board.dto.comment.query.CommentQueryDto;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentQueryDto> getCommentsByBoardId(Long boardId);

    Page<CommentQueryDto> getCommentsPageByBoardId(Long boardId, Pageable pageable);

    CheckPasswordDto getPassword(Long id);

    void deleteByBoardId(Long boardId);

    void deleteByMemberId(Long memberId);

    void deleteByBoardIdList(List<Long> boardIdList);

}
