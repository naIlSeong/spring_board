package toyproject.board.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.board.domain.member.Member;

@NoArgsConstructor
@Getter
@Setter
public class DeleteBoardDto {

    private Long id;
    private Member member;
    private String password;

    @Builder
    public DeleteBoardDto(Long id, Member member, String password) {
        this.id = id;
        this.member = member;
        this.password = password;
    }
}
