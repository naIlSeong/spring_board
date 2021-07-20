package toyproject.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.domain.member.Member;

@Getter
@Setter
public class JoinRequestDto {

    private String username;
    private String password;

    @Builder
    public JoinRequestDto(String username, String password) {
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
