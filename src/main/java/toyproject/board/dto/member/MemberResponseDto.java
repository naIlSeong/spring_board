package toyproject.board.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import toyproject.board.dto.BasicResponseDto;

@Getter
@Setter
@SuperBuilder
public class MemberResponseDto extends BasicResponseDto {

    private MemberNoPw member;

}
