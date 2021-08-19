package toyproject.board.dto.board.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.board.BoardNoPw;

@SuperBuilder
@Getter
@Setter
public class BoardListResponseDto extends BasicResponseDto {

    private Page<BoardNoPw> boardList;

}
