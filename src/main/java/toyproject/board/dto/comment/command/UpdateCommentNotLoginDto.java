package toyproject.board.dto.comment.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCommentNotLoginDto {

    @NotNull
    private Long commentId;

    @NotBlank
    private String content;

    @NotBlank
    private String password;

    @Builder
    public UpdateCommentNotLoginDto(Long commentId, String content, String password) {
        this.commentId = commentId;
        this.content = content;
        this.password = password;
    }
}
