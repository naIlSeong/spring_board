package toyproject.board.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import toyproject.board.dto.BasicResponseDto;

@SuperBuilder
@Getter
@Setter
public class BoardListResponseDto extends BasicResponseDto {

    private Page<BoardNoPw> boardList;

}
