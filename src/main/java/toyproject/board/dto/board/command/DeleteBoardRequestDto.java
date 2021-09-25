package toyproject.board.dto.board.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.board.domain.member.Member;

// 게시물 삭제 요청 DTO
@NoArgsConstructor
@Getter
@Setter
public class DeleteBoardRequestDto {

    private Long id;

    private Member member;

    private String password;

    @Builder
    public DeleteBoardRequestDto(Long id, Member member, String password) {
        this.id = id;
        this.member = member;
        this.password = password;
    }

    public DeleteBoardLoginDto toDto(Member member) {
        return DeleteBoardLoginDto.builder()
                .id(id)
                .member(member)
                .build();
    }

    public DeleteBoardNotLoginDto toDto() {
        return DeleteBoardNotLoginDto.builder()
                .id(id)
                .password(password)
                .build();
    }

}
