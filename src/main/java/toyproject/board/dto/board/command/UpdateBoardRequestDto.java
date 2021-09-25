package toyproject.board.dto.board.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.board.domain.member.Member;

// 게시물 수정 요청 DTO
@NoArgsConstructor
@Getter
@Setter
public class UpdateBoardRequestDto {

    private Long id;

    private Member member;

    private String password;

    private String title;

    private String content;

    @Builder
    public UpdateBoardRequestDto(Long id, Member member, String password, String title, String content) {
        this.id = id;
        this.member = member;
        this.password = password;
        this.title = title;
        this.content = content;
    }

    public UpdateBoardLoginDto toDto(Member member) {
        return UpdateBoardLoginDto.builder()
                .id(id)
                .member(member)
                .title(title)
                .content(content)
                .build();
    }

    public UpdateBoardNotLoginDto toDto() {
        return UpdateBoardNotLoginDto.builder()
                .id(id)
                .password(password)
                .title(title)
                .content(content)
                .build();
    }

}
