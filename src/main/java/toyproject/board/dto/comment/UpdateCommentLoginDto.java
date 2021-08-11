package toyproject.board.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.domain.member.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCommentLoginDto {

    @NotNull
    private Long commentId;

    @NotBlank
    private String content;

    @NotBlank
    private Member member;

    @Builder
    public UpdateCommentLoginDto(Long commentId, String content, Member member) {
        this.commentId = commentId;
        this.content = content;
        this.member = member;
    }

}
