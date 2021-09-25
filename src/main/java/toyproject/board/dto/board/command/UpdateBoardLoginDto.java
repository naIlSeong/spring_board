package toyproject.board.dto.board.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.domain.member.Member;

import javax.validation.constraints.NotNull;

// 로그인 게시물 수정 DTO
@Getter
@Setter
public class UpdateBoardLoginDto {

    @NotNull
    private Long id;

    @NotNull
    private Member member;

    private String title;
    private String content;

    @Builder
    public UpdateBoardLoginDto(Long id, Member member, String title, String content) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
    }
}
