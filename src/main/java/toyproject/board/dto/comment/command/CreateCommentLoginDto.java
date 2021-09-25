package toyproject.board.dto.comment.command;

import lombok.*;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.member.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// 로그인 댓글 생성 DTO
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class CreateCommentLoginDto {

    @NotNull
    private Long boardId;

    @NotBlank
    private String content;

    @NotNull
    private Member member;

    @Builder
    public CreateCommentLoginDto(Long boardId, String content, Member member) {
        this.boardId = boardId;
        this.content = content;
        this.member = member;
    }

    public Comment toEntity(Board board) {
        return Comment.builder()
                .board(board)
                .content(content)
                .nickname(member.getUsername())
                .member(member)
                .build();
    }

}
