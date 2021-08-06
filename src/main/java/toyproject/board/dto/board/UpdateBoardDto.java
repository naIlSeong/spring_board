package toyproject.board.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import toyproject.board.domain.member.Member;
import toyproject.board.marker.Login;
import toyproject.board.marker.NotLogin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class UpdateBoardDto {

    @NotNull
    private Long id;

    @NotNull(groups = Login.class)
    private Member member;

    @NotBlank(groups = NotLogin.class)
    @Length(min = 4, groups = NotLogin.class)
    private String password;

    private String title;
    private String content;

    @Builder
    public UpdateBoardDto(Long id, Member member, String password, String title, String content) {
        this.id = id;
        this.member = member;
        this.password = password;
        this.title = title;
        this.content = content;
    }
}
