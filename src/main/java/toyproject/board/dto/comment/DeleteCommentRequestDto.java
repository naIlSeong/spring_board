package toyproject.board.dto.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.board.domain.member.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class DeleteCommentRequestDto {

    private Long id;

    private String password;

    public DeleteCommentLoginDto toDto(Member member) {
        return DeleteCommentLoginDto.builder()
                .id(id)
                .member(member)
                .build();
    }

    public DeleteCommentNotLoginDto toDto() {
        return DeleteCommentNotLoginDto.builder()
                .id(id)
                .password(password)
                .build();
    }

}
