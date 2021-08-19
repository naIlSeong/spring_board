package toyproject.board.dto.board.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.board.query.BoardDetail;

@Getter
@Setter
@SuperBuilder
public class BoardDetailResponseDto extends BasicResponseDto {

    private BoardDetail boardDetail;

}
