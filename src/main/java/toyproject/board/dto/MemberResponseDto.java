package toyproject.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import toyproject.board.domain.member.Member;

@Getter
@Setter
@SuperBuilder
public class MemberResponseDto extends BasicResponseDto {

    private Member member;

}
