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
public class UpdateBoardRequestDto {

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
    public UpdateBoardRequestDto(Long id, Member member, String password, String title, String content) {
        this.id = id;
        this.member = member;
        this.password = password;
        this.title = title;
        this.content = content;
    }

    public UpdateBoardLoginDto toDto(Member member) {
        return UpdateBoardLoginDto.builder()
                .id(id)
                .member(member)
                .title(title)
                .content(content)
                .build();
    }

    public UpdateBoardNotLoginDto toDto() {
        return UpdateBoardNotLoginDto.builder()
                .id(id)
                .password(password)
                .title(title)
                .content(content)
                .build();
    }

}
