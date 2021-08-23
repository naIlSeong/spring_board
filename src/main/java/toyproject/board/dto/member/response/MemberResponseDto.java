package toyproject.board.dto.member.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.member.query.MemberQueryDto;

@Getter
@Setter
@SuperBuilder
public class MemberResponseDto extends BasicResponseDto {

    private MemberQueryDto member;

}
