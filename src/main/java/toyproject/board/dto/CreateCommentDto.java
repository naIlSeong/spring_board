package toyproject.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.member.Member;
import toyproject.board.marker.Login;
import toyproject.board.marker.NotLogin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateCommentDto {

    @NotNull
    private Long boardId;

    @NotBlank
    private String content;

    @NotBlank(groups = NotLogin.class)
    private String nickname;

    @NotBlank(groups = NotLogin.class)
    private String password;

    @NotNull(groups = Login.class)
    private Member member;

    @Builder
    public CreateCommentDto(Long boardId, String content, String nickname, String password, Member member) {
        this.boardId = boardId;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.member = member;
    }

    public Comment toEntity(Board board) {
        return Comment.builder()
                .board(board)
                .content(content)
                .nickname(nickname)
                .password(password)
                .member(member)
                .build();
    }

}
