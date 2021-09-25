package toyproject.board.dto.comment.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import toyproject.board.dto.BasicResponseDto;

// 댓글 생성, 수정 응답 DTO
@Getter
@Setter
@SuperBuilder
public class CommentResponseDto extends BasicResponseDto {

    private Long commentId;

}
