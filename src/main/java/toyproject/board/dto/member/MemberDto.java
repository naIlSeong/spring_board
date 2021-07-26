package toyproject.board.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.board.domain.member.Member;

@NoArgsConstructor
@Getter
@Setter
public class MemberDto {

    private String username;
    private String password;

    @Builder
    public MemberDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .build();
    }

}
