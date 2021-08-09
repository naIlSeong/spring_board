package toyproject.board.dto.comment;

import lombok.*;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.member.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class CreateCommentRequestDto {

    private Long boardId;

    private String content;

    private String nickname;

    private String password;

    private Member member;

    @Builder
    public CreateCommentRequestDto(Long boardId, String content, String nickname, String password, Member member) {
        this.boardId = boardId;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.member = member;
    }

    public Comment toEntity(Board board) {
        return Comment.builder()
                .board(board)
                .content(content)
                .nickname(nickname)
                .password(password)
                .member(member)
                .build();
    }

     //===createComment 메서드의 파라미터로 전달하기 위한 DTO 변환 메서드===//
    public CreateCommentLoginDto toDto(Member member) {
        return CreateCommentLoginDto.builder()
                .boardId(boardId)
                .content(content)
                .member(member)
                .build();
    }

    public CreateCommentNotLoginDto toDto() {
        return CreateCommentNotLoginDto.builder()
                .boardId(boardId)
                .content(content)
                .nickname(nickname)
                .password(password)
                .build();
    }

}
