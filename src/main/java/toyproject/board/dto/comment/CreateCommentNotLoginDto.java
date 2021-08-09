package toyproject.board.dto.comment;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.comment.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class CreateCommentNotLoginDto {

    @NotNull
    private Long boardId;

    @NotBlank
    private String content;

    @NotBlank
    @Length(min = 2, max = 24)
    private String nickname;

    @NotBlank
    @Length(min = 4)
    private String password;

    @Builder
    public CreateCommentNotLoginDto(Long boardId, String content, String nickname, String password) {
        this.boardId = boardId;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
    }

    public Comment toEntity(Board board) {
        return Comment.builder()
                .board(board)
                .content(content)
                .nickname(nickname)
                .password(password)
                .build();
    }

}
