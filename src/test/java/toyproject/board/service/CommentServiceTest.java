package toyproject.board.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.comment.CreateCommentLoginDto;
import toyproject.board.dto.comment.CreateCommentNotLoginDto;
import toyproject.board.dto.comment.CreateCommentRequestDto;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    EntityManager em;

    private Long boardId;
    private Long memberId;

    @BeforeEach
    public void beforeEach() {

        // 멤버 생성
        Member member = Member.builder()
                .username("test member")
                .password("1234")
                .build();
        em.persist(member);
        this.memberId = member.getId();

        // 게시물 생성
        Board board = Board.builder()
                .title("test title")
                .content("test content")
                .nickname("test nickname")
                .password("1234")
                .build();
        em.persist(board);
        this.boardId = board.getId();

    }

    @Tag("createComment")
    @Test
    void 댓글_생성_로그인() throws Exception {
        // give
        Member member = em.find(Member.class, memberId);

        CreateCommentRequestDto requestDto = CreateCommentRequestDto.builder()
                .boardId(boardId)
                .member(member)
                .content("test comment")
                .build();

        CreateCommentLoginDto dto = requestDto.toDto(member);

        // when
        Long commentId = commentService.createComment(dto);

        // then
        Comment comment = em.find(Comment.class, commentId);

        assertThat(comment.getMember().getId()).isEqualTo(memberId);
        assertThat(comment.getBoard().getId()).isEqualTo(boardId);
        assertThat(comment.getContent()).isEqualTo("test comment");
        assertThat(comment.getNickname()).isEqualTo(member.getUsername());
    }

    @Tag("createComment")
    @Test
    void 댓글_생성_비로그인() throws Exception {
        // give
        CreateCommentRequestDto requestDto = CreateCommentRequestDto.builder()
                .boardId(boardId)
                .nickname("comment nickname")
                .password("1234")
                .content("test comment")
                .build();

        CreateCommentNotLoginDto dto = requestDto.toDto();

        // when
        Long commentId = commentService.createComment(dto);

        // then
        Comment comment = em.find(Comment.class, commentId);

        assertThat(comment.getBoard().getId()).isEqualTo(boardId);
        assertThat(comment.getContent()).isEqualTo("test comment");
        assertThat(comment.getNickname()).isEqualTo("comment nickname");
    }

}