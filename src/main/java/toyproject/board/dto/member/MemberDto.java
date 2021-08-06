package toyproject.board.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import toyproject.board.domain.member.Member;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class MemberDto {

    @NotBlank
    @Length(min = 4, max = 24)
    private String username;

    @NotBlank
    @Length(min = 8)
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
