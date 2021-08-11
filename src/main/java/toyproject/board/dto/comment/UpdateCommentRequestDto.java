package toyproject.board.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.domain.member.Member;

@Getter
@Setter
public class UpdateCommentRequestDto {

    private Long commentId;
    private String content;
    private Member member;
    private String password;

    @Builder
    public UpdateCommentRequestDto(Long commentId, String content, Member member, String password) {
        this.commentId = commentId;
        this.content = content;
        this.member = member;
        this.password = password;
    }

    public UpdateCommentLoginDto toDto(Member member) {
        return UpdateCommentLoginDto.builder()
                .commentId(commentId)
                .content(content)
                .member(member)
                .build();
    }

    public UpdateCommentNotLoginDto toDto() {
        return UpdateCommentNotLoginDto.builder()
                .commentId(commentId)
                .content(content)
                .password(password)
                .build();
    }

}
