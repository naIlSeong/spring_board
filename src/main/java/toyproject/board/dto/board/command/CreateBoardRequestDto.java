package toyproject.board.dto.board.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.board.domain.member.Member;

// 게시물 생성 요청 DTO
@NoArgsConstructor
@Getter
@Setter
public class CreateBoardRequestDto {

    private String title;

    private String content;

    private Member member;

    private String nickname;

    private String password;

    @Builder
    public CreateBoardRequestDto(String title, String content, Member member, String nickname, String password) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.nickname = nickname;
        this.password = password;
    }

    public CreateBoardLoginDto toDto(Member member) {
        return CreateBoardLoginDto.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();

    }

    public CreateBoardNotLoginDto toDto() {
        return CreateBoardNotLoginDto.builder()
                .title(title)
                .content(content)
                .nickname(nickname)
                .password(password)
                .build();
    }

}
