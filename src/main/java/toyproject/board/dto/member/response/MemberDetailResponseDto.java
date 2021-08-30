package toyproject.board.dto.member.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.board.query.BoardAndCommentCount;
import toyproject.board.dto.member.query.MemberQueryDto;

@Getter
@Setter
@SuperBuilder
public class MemberDetailResponseDto extends BasicResponseDto {

    private MemberQueryDto member;
    private Page<BoardAndCommentCount> boardList;

}
