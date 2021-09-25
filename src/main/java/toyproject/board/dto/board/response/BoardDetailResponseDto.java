package toyproject.board.dto.board.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.board.query.BoardQueryDto;
import toyproject.board.dto.comment.query.CommentQueryDto;

// 게시물 상세 조회 응답 DTO
@Getter
@Setter
@SuperBuilder
public class BoardDetailResponseDto extends BasicResponseDto {

    private BoardQueryDto board;
    private Page<CommentQueryDto> comments;

}
