package toyproject.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MemberResponseDto extends BasicResponseDto {

    private MemberNoPw member;

}
