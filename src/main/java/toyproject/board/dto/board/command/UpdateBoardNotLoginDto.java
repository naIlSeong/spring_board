package toyproject.board.dto.board.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateBoardNotLoginDto {

    @NotNull
    private Long id;

    @NotBlank
    @Length(min = 4)
    private String password;

    private String title;
    private String content;

    @Builder
    public UpdateBoardNotLoginDto(Long id, String password, String title, String content) {
        this.id = id;
        this.password = password;
        this.title = title;
        this.content = content;
    }
}
