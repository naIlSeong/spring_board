package toyproject.board.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DeleteBoardNotLoginDto {

    @NotNull
    private Long id;

    @NotBlank
    @Length(min = 4)
    private String password;

    @Builder
    public DeleteBoardNotLoginDto(Long id, String password) {
        this.id = id;
        this.password = password;
    }

}
