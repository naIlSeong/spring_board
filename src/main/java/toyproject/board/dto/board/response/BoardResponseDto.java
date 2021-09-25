package toyproject.board.dto.board.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import toyproject.board.dto.BasicResponseDto;

// 게시물 생성, 수정 응답 DTO
@Getter
@Setter
@SuperBuilder
public class BoardResponseDto extends BasicResponseDto {

    private Long boardId;

}
