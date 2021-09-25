package toyproject.board.dto.board.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import toyproject.board.domain.member.Member;

import javax.validation.constraints.NotNull;

// 로그인 게시물 삭제 DTO
@Getter
@Setter
public class DeleteBoardLoginDto {

    @NotNull
    private Long id;

    @NotNull
    private Member member;

    @Builder
    public DeleteBoardLoginDto(Long id, Member member) {
        this.id = id;
        this.member = member;
    }
}