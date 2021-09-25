package toyproject.board.dto.board.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import toyproject.board.domain.board.Board;

import javax.validation.constraints.NotBlank;

// 비로그인 게시물 생성 DTO
@Getter
@Setter
public class CreateBoardNotLoginDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @Length(min = 2, max = 24)
    private String nickname;

    @NotBlank
    @Length(min = 4)
    private String password;

    @Builder
    public CreateBoardNotLoginDto(String title, String content, String nickname, String password) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .nickname(nickname)
                .password(password)
                .build();
    }

}
