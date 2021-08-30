package toyproject.board.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.board.command.*;
import toyproject.board.dto.board.query.BoardAndCommentCount;
import toyproject.board.dto.board.query.BoardQueryDto;
import toyproject.board.dto.board.query.BoardSearchCondition;
import toyproject.board.dto.member.command.MemberRequestDto;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    EntityManager em;

    /**
     * 게시물 작성 (생성) 테스트
     * boardService.createBoard();
     */
    @Tag("createBoard")
    @Test
    void 게시물_작성_성공_로그인O() throws Exception {
        // give
        Member member = Member.builder()
                .username("test_member")
                .password("1234")
                .build();
        em.persist(member);

        CreateBoardLoginDto dto = CreateBoardLoginDto.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();

        // when
        Long boardId = boardService.createBoard(dto);

        em.flush();
        em.clear();

        // then
        Board result = em.find(Board.class, boardId);

        assertThat(result.getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.getContent()).isEqualTo(dto.getContent());

        assertThat(result.getMember().getId()).isEqualTo(member.getId());
        assertThat(result.getMember().getUsername()).isEqualTo(member.getUsername());
    }

    @Tag("createBoard")
    @Test
    void 게시물_작성_성공_로그인X() throws Exception {
        // give
        CreateBoardNotLoginDto dto = CreateBoardNotLoginDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test nickname")
                .password("1234")
                .build();

        // when
        Long boardId = boardService.createBoard(dto);

        em.flush();
        em.clear();

        // then
        Board result = em.find(Board.class, boardId);

        assertThat(result.getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.getContent()).isEqualTo(dto.getContent());

        assertThat(result.getNickname()).isEqualTo(dto.getNickname());
        assertThat(BCrypt.checkpw("1234", result.getPassword())).isTrue();
    }

    /**
     * 게시물 삭제 테스트
     * boardService.deleteBoard();
     */
    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_로그인() throws Exception {
        // give
        Member member = Member.builder()
                .username("test")
                .password("12341234")
                .build();
        em.persist(member);

        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        em.persist(board);

        em.flush();
        em.clear();

        Long boardId = board.getId();

        // when
        DeleteBoardLoginDto dto = DeleteBoardLoginDto.builder()
                .id(boardId)
                .member(member)
                .build();
        boardService.deleteBoard(dto);

        // then
        Board result = em.find(Board.class, boardId);
        assertThat(result).isNull();
    }

    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_비로그인() throws Exception {
        // give
        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                .build();
        em.persist(board);

        Long boardId = board.getId();

        // when
        DeleteBoardNotLoginDto dto = DeleteBoardNotLoginDto.builder()
                .id(boardId)
                .password("1234")
                .build();
        boardService.deleteBoard(dto);

        // then
        Board result = em.find(Board.class, boardId);
        assertThat(result).isNull();
    }

    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_실패_게시물_존재X() throws Exception {
        // give


        // when
        DeleteBoardNotLoginDto dto = DeleteBoardNotLoginDto.builder()
                .id(123L)
                .password("1234")
                .build();
        // boardService.deleteBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.deleteBoard(dto))
                .hasMessage("게시물을 찾을 수 없습니다.");
    }

    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_실패_다른사람의_게시물() throws Exception {
        // give
        Member member = Member.builder()
                .username("test")
                .password("1234")
                .build();
        Member otherMember = Member.builder()
                .username("other")
                .password("1234")
                .build();

        em.persist(member);
        em.persist(otherMember);

        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        em.persist(board);

        em.flush();
        em.clear();

        Long boardId = board.getId();

        // when
        DeleteBoardLoginDto dto = DeleteBoardLoginDto.builder()
                .id(boardId)
                .member(otherMember)
                .build();
        // boardService.deleteBoard(dto);

        // then
        Board result = em.find(Board.class, boardId);

        assertThatThrownBy(() -> boardService.deleteBoard(dto))
                .hasMessage("게시물을 삭제할 수 없습니다.");
        assertThat(result.getMember().getId()).isNotEqualTo(boardId);
    }

    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_실패_비밀번호_틀림() throws Exception {
        // give
        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                .build();
        em.persist(board);

        em.flush();
        em.clear();

        Long boardId = board.getId();

        // when
        DeleteBoardNotLoginDto dto = DeleteBoardNotLoginDto.builder()
                .id(boardId)
                .password("6789")
                .build();
        // boardService.deleteBoard(dto);

        // then
        Board result = em.find(Board.class, boardId);

        assertThatThrownBy(() -> boardService.deleteBoard(dto))
                .hasMessage("비밀번호를 다시 확인해 주세요.");
        assertThat(BCrypt.checkpw("6789", result.getPassword())).isFalse();
    }

    /**
     * 게시물 수정 테스트
     * boardService.updateBoard();
     */
    @Tag("updateBoard")
    @Test
    void 게시물_수정_로그인() throws Exception {
        // give
        Member member = Member.builder()
                .username("test")
                .password("12341234")
                .build();
        em.persist(member);

        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        em.persist(board);

        Long boardId = board.getId();

        // when
        UpdateBoardLoginDto dto = UpdateBoardLoginDto.builder()
                .id(boardId)
                .member(member)
                .title("updated title.")
                .content("updated content.")
                .build();
        boardService.updateBoard(dto);

        em.flush();
        em.clear();

        // then
        Board result = em.find(Board.class, boardId);

        assertThat(result.getTitle()).isEqualTo("updated title.");
        assertThat(result.getContent()).isEqualTo("updated content.");
        assertThat(result.getPassword()).isNull();
    }

    @Tag("updateBoard")
    @Test
    void 게시물_수정_비로그인() throws Exception {
        // give
        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                .build();
        em.persist(board);

        Long boardId = board.getId();

        // when
        UpdateBoardNotLoginDto dto = UpdateBoardNotLoginDto.builder()
                .id(boardId)
                .password("1234")
                .title("updated title.")
                .content("updated content.")
                .build();
        boardService.updateBoard(dto);

        em.flush();
        em.clear();

        // then
        Board result = em.find(Board.class, boardId);

        assertThat(result.getTitle()).isEqualTo("updated title.");
        assertThat(result.getContent()).isEqualTo("updated content.");
        assertThat(result.getMember()).isNull();
    }

    @Tag("updateBoard")
    @Test
    void 게시물_수정_비로그인_비밀번호_틀림() throws Exception {
        // give
        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                .build();
        em.persist(board);

        Long boardId = board.getId();

        // when
        UpdateBoardNotLoginDto dto = UpdateBoardNotLoginDto.builder()
                .id(boardId)
                .password("5432")
                .title("updated title.")
                .content("updated content.")
                .build();
        // boardService.updateBoardNotLogin(dto);

        em.flush();
        em.clear();

        // then
        assertThatThrownBy(() -> boardService.updateBoard(dto))
                .hasMessage("비밀번호를 다시 확인해 주세요.");
    }

    @Tag("updateBoard")
    @Test
    void 게시물_수정_로그인_다른_멤버의_게시물_삭제_시도() throws Exception {
        // give
        Member member = Member.builder()
                .username("test")
                .password("12341234")
                .build();
        Member otherMember = Member.builder()
                .username("otherMember")
                .password("12341234")
                .build();

        em.persist(member);
        em.persist(otherMember);

        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        em.persist(board);

        Long boardId = board.getId();

        // when
        UpdateBoardLoginDto dto = UpdateBoardLoginDto.builder()
                .id(boardId)
                .member(otherMember)
                .title("updated title.")
                .content("updated content.")
                .build();
        // boardService.updateBoardLogin(dto);

        em.flush();
        em.clear();

        // then
        assertThatThrownBy(() -> boardService.updateBoard(dto))
                .hasMessage("게시물을 수정할 수 없습니다.");
    }

    @Tag("getBoard")
    @Test
    void 게시물_조회_성공() throws Exception {
        // give
        Board board = Board.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password("1234")
                .build();
        em.persist(board);

        em.flush();
        em.clear();

        Long boardId = board.getId();

        // when
        BoardQueryDto result = boardService.getBoard(boardId);

        // then
        assertThat(result.getTitle()).isEqualTo("test title.");
        assertThat(result.getContent()).isEqualTo("test content.");
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Tag("getBoard")
    @Test
    void 게시물_조회_실패() throws Exception {
        // give

        // when
        // boardService.getBoard(1212L);

        // then
        assertThatThrownBy(() -> boardService.getBoard(1212L))
                .hasMessage("게시물을 찾을 수 없습니다.");
    }

    /*
    @Tag("getBoardList")
    @Test
    void 게시물_조회() throws Exception {
        // give
        for (int i = 0; i < 100; i++) {
            Board board = Board.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .nickname("nickname" + i)
                    .password("1234")
                    .build();
            em.persist(board);
        }

        // when
        Pageable pageable = PageRequest.of(0, 20);
        Page<BoardQueryDto> result = boardService.getBoardList(pageable, null);

        // then
        assertThat(result.getTotalElements()).isEqualTo(100);
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("title0");
    }

    @Tag("getBoardList")
    @Test
    void 게시물_조회_마지막_페이지_반환() throws Exception {
        // give
        for (int i = 0; i < 100; i++) {
            Board board = Board.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .nickname("nickname" + i)
                    .password("1234")
                    .build();
            em.persist(board);
        }

        // when
        Pageable pageable = PageRequest.of(10, 20);
        Page<BoardQueryDto> result = boardService.getBoardList(pageable, null);

        // then
        assertThat(result.getTotalElements()).isEqualTo(100);
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(4);
        assertThat(result.isLast()).isTrue();
        assertThat(result.getContent().size()).isEqualTo(20);
        assertThat(result.getContent().get(19).getTitle()).isEqualTo("title99");
    }

    @Tag("getBoardList")
    @Test
    void 게시물_조회_게시물X() throws Exception {
        // give

        // when
        Pageable pageable = PageRequest.of(10, 20);
        // boardService.getBoardList(pageable, null);

        // then
        assertThatThrownBy(() -> boardService.getBoardList(pageable, null))
                .hasMessage("게시물이 없습니다.");
    }

    @Tag("getBoardList")
    @Test
    void 게시물_조회_with_member_id_게시물X() throws Exception {
        // give
        Board board = Board.builder()
                .title("title")
                .content("content")
                .nickname("nickname")
                .password("1234")
                .build();
        em.persist(board);

        Member member = Member.builder()
                .username("test")
                .password("12341234")
                .build();
        em.persist(member);

        // when
        Pageable pageable = PageRequest.of(10, 20);
        // boardService.getBoardList(pageable, null);

        // then
        assertThatThrownBy(() -> boardService.getBoardList(pageable, member.getId()))
                .hasMessage("게시물이 없습니다.");

    }


*/

    @Tag("getBoardList()")
    @Test
    void 게시물_리스트() throws Exception {
        // give
        Member testMember = MemberRequestDto.builder()
                .username("test member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(testMember);

        Member bestMember = MemberRequestDto.builder()
                .username("best member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(bestMember);

        for (int i = 1; i <= 20; i++) {

            Board.BoardBuilder builder = Board.builder();

            if (i % 2 == 0) {
                builder = builder
                        .title("best title - " + i)
                        .content("best content - " + i)
                        .nickname(bestMember.getUsername())
                        .member(bestMember);
            } else {
                builder = builder
                        .title("test title - " + i)
                        .content("test content - " + i)
                        .nickname(testMember.getUsername())
                        .member(testMember);
            }

            em.persist(builder.build());
        }

        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(1, 3);

        Page<BoardAndCommentCount> result = boardService.getBoardList(pageable);

        // then
        long total = result.getTotalElements();
        List<BoardAndCommentCount> content = result.getContent();

        assertThat(total).isEqualTo(20L);
        assertThat(content.size()).isEqualTo(3);
        assertThat(content)
                .extracting("content")
                .containsExactly("test content - 17", "best content - 16", "test content - 15");
    }

    @Tag("getBoardList()")
    @Test
    void 게시물_리스트_마지막_페이지_반환() throws Exception {
        // give
        Member bestMember = Member.builder()
                .username("best member")
                .password("12341234")
                .build();
        em.persist(bestMember);

        for (int i = 1; i <= 21; i++) {

            Board board = Board.builder()
                    .title("title - " + i)
                    .content("content - " + i)
                    .nickname(bestMember.getUsername())
                    .member(bestMember)
                    .build();

            em.persist(board);
        }

        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(4, 10);

        Page<BoardAndCommentCount> result = boardService.getBoardList(pageable);

        // then
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getNumber()).isEqualTo(2);
        assertThat(result.isLast()).isTrue();

        assertThat(result.getTotalElements()).isEqualTo(21);
        assertThat(result.getNumberOfElements()).isEqualTo(1);

        assertThat(result.getContent().get(0).getContent()).isEqualTo("content - 1");
    }

    @Tag("getBoardList")
    @Test
    void 게시물_리스트_실패() throws Exception {
        // give

        // when
        Pageable pageable = PageRequest.of(0, 20);
        // boardService.getBoardList(pageable);

        // then
        assertThatThrownBy(() -> boardService.getBoardList(pageable))
                .hasMessage("게시물이 없습니다.");
    }

    @Tag("searchBoardList")
    @Test
    void 게시물_검색() throws Exception {
        // give
        Member testMember = MemberRequestDto.builder()
                .username("test member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(testMember);

        Member bestMember = MemberRequestDto.builder()
                .username("best member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(bestMember);

        for (int i = 1; i <= 20; i++) {

            Board.BoardBuilder builder = Board.builder();

            if (i % 2 == 0) {
                builder = builder
                        .title("best title - " + i)
                        .content("best content - " + i)
                        .nickname(bestMember.getUsername())
                        .member(bestMember);
            } else {
                builder = builder
                        .title("test title - " + i)
                        .content("test content - " + i)
                        .nickname(testMember.getUsername())
                        .member(testMember);
            }

            em.persist(builder.build());
        }

        em.flush();
        em.clear();

        // 검색 조건
        BoardSearchCondition condition = BoardSearchCondition.builder()
                .content("1")
                .nickname("best")
                .build();

        // Pageable
        PageRequest pageable = PageRequest.of(0, 3);

        // when
        Page<BoardAndCommentCount> result = boardService.searchBoard(condition, pageable);

        // then
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.isFirst()).isTrue();

        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getNumberOfElements()).isEqualTo(3);

        assertThat(result.getContent().get(0).getContent()).isEqualTo("best content - 18");
    }

    @Tag("searchBoardList")
    @Test
    void 게시물_검색_마지막_페이지_반환() throws Exception {
        // give
        Member testMember = MemberRequestDto.builder()
                .username("test member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(testMember);

        Member bestMember = MemberRequestDto.builder()
                .username("best member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(bestMember);

        for (int i = 1; i <= 20; i++) {

            Board.BoardBuilder builder = Board.builder();

            if (i % 2 == 0) {
                builder = builder
                        .title("best title - " + i)
                        .content("best content - " + i)
                        .nickname(bestMember.getUsername())
                        .member(bestMember);
            } else {
                builder = builder
                        .title("test title - " + i)
                        .content("test content - " + i)
                        .nickname(testMember.getUsername())
                        .member(testMember);
            }

            em.persist(builder.build());
        }

        em.flush();
        em.clear();

        // 검색 조건
        BoardSearchCondition condition = BoardSearchCondition.builder()
                .content("1")
                .nickname("best")
                .build();

        // Pageable
        PageRequest pageable = PageRequest.of(4, 3);

        // when
        Page<BoardAndCommentCount> result = boardService.searchBoard(condition, pageable);

        // then
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.isLast()).isTrue();

        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getNumberOfElements()).isEqualTo(2);

        assertThat(result.getContent().get(0).getContent()).isEqualTo("best content - 12");
    }

    @Tag("searchBoardList")
    @Test
    void 게시물_검색_결과_없음() throws Exception {
        // give
        for (int i = 1; i <= 20; i++) {

            Board board = Board.builder()
                    .title("title " + i)
                    .content("content" + i)
                    .nickname("user " + i)
                    .password("1234")
                    .build();
            em.persist(board);

        }

        em.flush();
        em.clear();

        // 검색 조건
        BoardSearchCondition condition = BoardSearchCondition.builder()
                .title("best")
                .build();

        // Pageable
        PageRequest pageable = PageRequest.of(0, 3);

        // when
        // boardService.searchBoard(condition, pageable);

        // then
        assertThatThrownBy(() -> boardService.searchBoard(condition, pageable))
                .hasMessage("게시물이 없습니다.");
    }

}