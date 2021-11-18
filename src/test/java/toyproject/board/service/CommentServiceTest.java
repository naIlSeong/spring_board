package toyproject.board.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.comment.command.*;
import toyproject.board.dto.comment.query.CommentQueryDto;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Tag("updateComment")
    @Test
    void 댓글_수정_로그인() throws Exception {
        // give
        Board board = em.find(Board.class, boardId);
        Member member = em.find(Member.class, memberId);

        Comment comment = Comment.builder()
                .board(board)
                .content("test comment")
                .member(member)
                .nickname(member.getUsername())
                .build();
        em.persist(comment);

        UpdateCommentLoginDto dto = UpdateCommentLoginDto.builder()
                .commentId(comment.getId())
                .content("updated comment")
                .member(member)
                .build();

        em.flush();
        em.clear();

        // when
        Long commentId = commentService.updateComment(dto);

        // then
        Comment result = em.find(Comment.class, commentId);

        assertThat(result.getContent()).isEqualTo("updated comment");
    }

    @Tag("updateComment")
    @Test
    void 댓글_수정_비로그인() throws Exception {
        // give
        Board board = em.find(Board.class, boardId);

        Comment comment = Comment.builder()
                .board(board)
                .content("test comment")
                .nickname("ㅇㅇ")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                .build();
        em.persist(comment);

        UpdateCommentNotLoginDto dto = UpdateCommentNotLoginDto.builder()
                .commentId(comment.getId())
                .content("updated comment")
                .password("1234")
                .build();

        em.flush();
        em.clear();

        // when
        Long commentId = commentService.updateComment(dto);

        // then
        Comment result = em.find(Comment.class, commentId);

        assertThat(result.getContent()).isEqualTo("updated comment");
        assertThat(result.getNickname()).isEqualTo("ㅇㅇ");
    }

    @Tag("updateComment")
    @Test
    void 댓글_수정_로그인_실패() throws Exception {
        // give
        Board board = em.find(Board.class, boardId);
        Member member = em.find(Member.class, memberId);

        Comment comment = Comment.builder()
                .board(board)
                .content("test comment")
                .member(member)
                .nickname(member.getUsername())
                .build();
        em.persist(comment);

        Member otherMember = Member.builder()
                .username("other member")
                .password("1234")
                .build();
        em.persist(otherMember);

        UpdateCommentLoginDto dto = UpdateCommentLoginDto.builder()
                .commentId(comment.getId())
                .content("updated comment")
                .member(otherMember)
                .build();

        em.flush();
        em.clear();

        // when
        // Long commentId = commentService.updateComment(dto);

        // then
        Comment result = em.find(Comment.class, comment.getId());

        assertThatThrownBy(() -> commentService.updateComment(dto))
                .hasMessage("댓글을 수정할 수 없습니다.");
        assertThat(result.getMember().getId()).isEqualTo(memberId);
    }

    @Tag("updateComment")
    @Test
    void 댓글_수정_비로그인_실패() throws Exception {
        // give
        Board board = em.find(Board.class, boardId);

        Comment comment = Comment.builder()
                .board(board)
                .content("test comment")
                .nickname("ㅇㅇ")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                .build();
        em.persist(comment);

        UpdateCommentNotLoginDto dto = UpdateCommentNotLoginDto.builder()
                .commentId(comment.getId())
                .content("updated comment")
                .password("5678")
                .build();

        em.flush();
        em.clear();

        // when
        // Long commentId = commentService.updateComment(dto);

        // then
        Comment result = em.find(Comment.class, comment.getId());

        assertThatThrownBy(() -> commentService.updateComment(dto))
                .hasMessage("비밀번호를 다시 확인해 주세요.");
        assertThat(result.getContent()).isEqualTo("test comment");
    }

    @Tag("deleteComment")
    @Test
    void 댓글_삭제_로그인() throws Exception {
        // give
        Member member = em.find(Member.class, memberId);
        Board board = em.find(Board.class, boardId);

        Comment comment = Comment.builder()
                .content("test comment")
                .member(member)
                .nickname(member.getUsername())
                .board(board)
                .build();
        em.persist(comment);

        DeleteCommentLoginDto dto = DeleteCommentLoginDto.builder()
                .id(comment.getId())
                .member(member)
                .build();

        // when
        commentService.deleteComment(dto);

        // then
        Comment result = em.find(Comment.class, comment.getId());
        assertThat(result).isNull();
    }

    @Tag("deleteComment")
    @Test
    void 댓글_삭제_비로그인() throws Exception {
        // give
        Board board = em.find(Board.class, boardId);

        Comment comment = Comment.builder()
                .content("test comment")
                .nickname("test nickname")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                .board(board)
                .build();
        em.persist(comment);

        DeleteCommentNotLoginDto dto = DeleteCommentNotLoginDto.builder()
                .id(comment.getId())
                .password("1234")
                .build();

        // when
        commentService.deleteComment(dto);

        // then
        Comment result = em.find(Comment.class, comment.getId());
        assertThat(result).isNull();
    }

    @Tag("deleteComment")
    @Test
    void 댓글_삭제_로그인_실패() throws Exception {
        // give
        Member member = em.find(Member.class, memberId);
        Board board = em.find(Board.class, boardId);

        Comment comment = Comment.builder()
                .content("test comment")
                .member(member)
                .nickname(member.getUsername())
                .board(board)
                .build();
        em.persist(comment);

        Member otherMember = Member.builder()
                .username("other member")
                .password("1234")
                .build();

        DeleteCommentLoginDto dto = DeleteCommentLoginDto.builder()
                .id(comment.getId())
                .member(otherMember)
                .build();

        // when
        // commentService.deleteComment(dto);

        // then
        assertThatThrownBy(() -> commentService.deleteComment(dto))
                .hasMessage("댓글을 삭제할 수 없습니다.");
    }

    @Tag("deleteComment")
    @Test
    void 댓글_삭제_비로그인_실패() throws Exception {
        // give
        Board board = em.find(Board.class, boardId);

        Comment comment = Comment.builder()
                .content("test comment")
                .nickname("test nickname")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                .board(board)
                .build();
        em.persist(comment);

        DeleteCommentNotLoginDto dto = DeleteCommentNotLoginDto.builder()
                .id(comment.getId())
                .password("5678")
                .build();

        // when
        // commentService.deleteComment(dto);

        // then
        assertThatThrownBy(() -> commentService.deleteComment(dto))
                .hasMessage("비밀번호를 다시 확인해 주세요.");
    }

    @Tag("getCommentsPage")
    @Test
    void 댓글_페이징_쿼리() throws Exception {
        // give
        Board board = em.find(Board.class, boardId);

        for (int i = 1; i <= 21; i++) {
            Comment comment = Comment.builder()
                    .content("comment " + i)
                    .board(board)
                    .nickname("user " + i)
                    .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                    .build();
            em.persist(comment);
        }

        // when
        PageRequest pageable = PageRequest.of(0, 10);
        Page<CommentQueryDto> result = commentService.getCommentsPage(boardId, pageable);

        // then
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.isFirst()).isTrue();

        assertThat(result.getTotalElements()).isEqualTo(21);
        assertThat(result.getNumberOfElements()).isEqualTo(10);

        assertThat(result.getContent().get(0).getContent()).isEqualTo("comment 1");
    }

    @Tag("getCommentsPage")
    @Test
    void 댓글_페이징_쿼리_마지막_페이지_반환() throws Exception {
        // give
        Board board = em.find(Board.class, boardId);

        for (int i = 1; i <= 21; i++) {
            Comment comment = Comment.builder()
                    .content("comment " + i)
                    .board(board)
                    .nickname("user " + i)
                    .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                    .build();
            em.persist(comment);
        }

        // when
        PageRequest pageable = PageRequest.of(4, 10);
        Page<CommentQueryDto> result = commentService.getCommentsPage(boardId, pageable);

        // then
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getNumber()).isEqualTo(2);
        assertThat(result.isLast()).isTrue();

        assertThat(result.getTotalElements()).isEqualTo(21);
        assertThat(result.getNumberOfElements()).isEqualTo(1);

        assertThat(result.getContent().get(0).getContent()).isEqualTo("comment 21");
    }

}