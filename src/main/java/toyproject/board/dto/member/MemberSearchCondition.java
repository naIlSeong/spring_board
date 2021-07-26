package toyproject.board.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//@NoArgsConstructor
@Getter
@Setter
public class MemberSearchCondition {

    private String username;

    @QueryProjection
    @Builder
    public MemberSearchCondition(String username) {
        this.username = username;
    }
}
