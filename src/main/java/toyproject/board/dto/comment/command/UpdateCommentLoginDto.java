package toyproject.board.dto.comment.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.domain.member.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// 로그인 댓글 수정 DTO
@Getter
@Setter
public class UpdateCommentLoginDto {

    @NotNull
    private Long commentId;

    @NotBlank
    private String content;

    @NotNull
    private Member member;

    @Builder
    public UpdateCommentLoginDto(Long commentId, String content, Member member) {
        this.commentId = commentId;
        this.content = content;
        this.member = member;
    }

}
