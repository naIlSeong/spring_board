package toyproject.board.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.board.domain.member.Member;

@NoArgsConstructor
@Getter
@Setter
public class UpdateBoardDto {

    private Long id;
    private Member member;
    private String password;
    private String title;
    private String content;

    @Builder
    public UpdateBoardDto(Long id, Member member, String password, String title, String content) {
        this.id = id;
        this.member = member;
        this.password = password;
        this.title = title;
        this.content = content;
    }
}
