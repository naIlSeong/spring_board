package toyproject.board.dto.member.command;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import toyproject.board.domain.member.Member;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class MemberRequestDto {

    @NotBlank
    @Length(min = 4, max = 24)
    private String username;

    @NotBlank
    @Length(min = 8)
    private String password;

    @Builder
    public MemberRequestDto(String username, String password) {
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
