package toyproject.board.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.member.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateBoardLoginDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Member member;

    private String nickname;

    @Builder
    public CreateBoardLoginDto(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .nickname(nickname)
                .build();
    }

}
