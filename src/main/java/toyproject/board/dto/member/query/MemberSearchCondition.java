package toyproject.board.dto.member.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchCondition {

    private String username;

    @QueryProjection
    public MemberSearchCondition(String username) {
        this.username = username;
    }
}
