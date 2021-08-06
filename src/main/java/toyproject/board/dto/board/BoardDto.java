package toyproject.board.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.member.Member;
import toyproject.board.marker.Login;
import toyproject.board.marker.NotLogin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class BoardDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull(groups = Login.class)
    private Member member;

    @NotBlank(groups = NotLogin.class)
    @Length(min = 2, max = 24, groups = NotLogin.class)
    private String nickname;

    @NotBlank(groups = NotLogin.class)
    @Length(min = 4, groups = NotLogin.class)
    private String password;

    @Builder
    public BoardDto(String title, String content, Member member, String nickname, String password) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.nickname = nickname;
        this.password = password;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .nickname(nickname)
                .password(password)
                .build();
    }

}
