package toyproject.board.dto.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import toyproject.board.dto.BasicResponseDto;

@Getter
@Setter
@SuperBuilder
public class CommentResponseDto extends BasicResponseDto {

    private Long commentId;

}
