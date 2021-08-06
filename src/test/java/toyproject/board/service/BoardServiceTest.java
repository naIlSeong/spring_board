package toyproject.board.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.board.*;
import toyproject.board.dto.member.MemberDto;

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

        BoardDto dto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();

        // when
        Long boardId = boardService.createBoardLogin(dto);

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
        BoardDto dto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test nickname")
                .password("1234")
                .build();

        // when
        Long boardId = boardService.createBoardNotLogin(dto);

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

        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        Long boardId = boardService.createBoardLogin(boardDto);

        em.flush();
        em.clear();

        // when
        DeleteBoardDto dto = DeleteBoardDto.builder()
                .id(boardId)
                .member(member)
                .build();
        boardService.deleteBoardLogin(dto);

        // then
        Board board = em.find(Board.class, boardId);
        assertThat(board).isNull();
    }

    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_비로그인() throws Exception {
        // give
        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password("1234")
                .build();
        Long boardId = boardService.createBoardNotLogin(boardDto);

        // when
        DeleteBoardDto dto = DeleteBoardDto.builder()
                .id(boardId)
                .password("1234")
                .build();
        boardService.deleteBoardNotLogin(dto);

        // then
        Board board = em.find(Board.class, boardId);
        assertThat(board).isNull();
    }

    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_실패_게시물_존재X() throws Exception {
        // give


        // when
        DeleteBoardDto dto = DeleteBoardDto.builder()
                .id(123L)
                .password("1234")
                .build();
        // boardService.deleteBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.deleteBoardNotLogin(dto))
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

        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        Long boardId = boardService.createBoardLogin(boardDto);

        em.flush();
        em.clear();

        // when
        DeleteBoardDto dto = DeleteBoardDto.builder()
                .id(boardId)
                .member(otherMember)
                .build();
        // boardService.deleteBoard(dto);

        // then
        Board board = em.find(Board.class, boardId);

        assertThatThrownBy(() -> boardService.deleteBoardLogin(dto))
                .hasMessage("게시물을 삭제할 수 없습니다.");
        assertThat(board.getMember().getId()).isNotEqualTo(boardId);
    }

    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_실패_비밀번호_틀림() throws Exception {
        // give
        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password("1234")
                .build();
        Long boardId = boardService.createBoardNotLogin(boardDto);

        em.flush();
        em.clear();

        // when
        DeleteBoardDto dto = DeleteBoardDto.builder()
                .id(boardId)
                .password("6789")
                .build();
        // boardService.deleteBoard(dto);

        // then
        Board board = em.find(Board.class, boardId);

        assertThatThrownBy(() -> boardService.deleteBoardNotLogin(dto))
                .hasMessage("비밀번호를 다시 확인해 주세요.");
        assertThat(BCrypt.checkpw("6789", board.getPassword())).isFalse();
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

        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        Long boardId = boardService.createBoardLogin(boardDto);

        // when
        UpdateBoardDto dto = UpdateBoardDto.builder()
                .id(boardId)
                .member(member)
                .title("updated title.")
                .content("updated content.")
                .build();
        boardService.updateBoardLogin(dto);

        em.flush();
        em.clear();

        // then
        Board board = em.find(Board.class, boardId);

        assertThat(board.getTitle()).isEqualTo("updated title.");
        assertThat(board.getContent()).isEqualTo("updated content.");
        assertThat(board.getPassword()).isNull();
    }

    @Tag("updateBoard")
    @Test
    void 게시물_수정_비로그인() throws Exception {
        // give
        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password("1234")
                .build();
        Long boardId = boardService.createBoardNotLogin(boardDto);

        // when
        UpdateBoardDto dto = UpdateBoardDto.builder()
                .id(boardId)
                .password("1234")
                .title("updated title.")
                .content("updated content.")
                .build();
        boardService.updateBoardNotLogin(dto);

        em.flush();
        em.clear();

        // then
        Board board = em.find(Board.class, boardId);

        assertThat(board.getTitle()).isEqualTo("updated title.");
        assertThat(board.getContent()).isEqualTo("updated content.");
        assertThat(board.getMember()).isNull();
    }

    @Tag("updateBoard")
    @Test
    void 게시물_수정_비로그인_비밀번호_틀림() throws Exception {
        // give
        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password("1234")
                .build();
        Long boardId = boardService.createBoardNotLogin(boardDto);

        // when
        UpdateBoardDto dto = UpdateBoardDto.builder()
                .id(boardId)
                .password("5432")
                .title("updated title.")
                .content("updated content.")
                .build();
        // boardService.updateBoardNotLogin(dto);

        em.flush();
        em.clear();

        // then
        assertThatThrownBy(() -> boardService.updateBoardNotLogin(dto))
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

        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        Long boardId = boardService.createBoardLogin(boardDto);

        // when
        UpdateBoardDto dto = UpdateBoardDto.builder()
                .id(boardId)
                .member(otherMember)
                .title("updated title.")
                .content("updated content.")
                .build();
        // boardService.updateBoardLogin(dto);

        em.flush();
        em.clear();

        // then
        assertThatThrownBy(() -> boardService.updateBoardLogin(dto))
                .hasMessage("게시물을 삭제할 수 없습니다.");
    }

    @Tag("getBoard")
    @Test
    void 게시물_조회_성공() throws Exception {
        // give
        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password("1234")
                .build();
        Long boardId = boardService.createBoardNotLogin(boardDto);

        em.flush();
        em.clear();

        // when
        BoardNoPw result = boardService.getBoard(boardId);

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

    @Tag("getBoardList")
    @Test
    void 게시물_조회() throws Exception {
        // give
        for (int i = 0; i < 100; i++) {
            BoardDto boardDto = BoardDto.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .nickname("nickname" + i)
                    .password("1234")
                    .build();

            Board board = boardDto.toEntity();
            em.persist(board);
        }

        // when
        Pageable pageable = PageRequest.of(0, 20);
        Page<BoardNoPw> result = boardService.getBoardList(pageable, null);

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
            BoardDto boardDto = BoardDto.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .nickname("nickname" + i)
                    .password("1234")
                    .build();

            Board board = boardDto.toEntity();
            em.persist(board);
        }

        // when
        Pageable pageable = PageRequest.of(10, 20);
        Page<BoardNoPw> result = boardService.getBoardList(pageable, null);

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
        BoardDto boardDto = BoardDto.builder()
                .title("title")
                .content("content")
                .nickname("nickname")
                .password("1234")
                .build();

        Board board = boardDto.toEntity();
        em.persist(board);

        MemberDto memberDto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();
        Member member = memberDto.toEntity();
        em.persist(member);

        // when
        Pageable pageable = PageRequest.of(10, 20);
        // boardService.getBoardList(pageable, null);

        // then
        assertThatThrownBy(() -> boardService.getBoardList(pageable, member.getId()))
                .hasMessage("게시물이 없습니다.");

    }

    @Tag("searchBoard")
    @Test
    void 게시물_검색_제목() throws Exception {
        // give
        Member testMember = MemberDto.builder()
                .username("test member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(testMember);

        Member bestMember = MemberDto.builder()
                .username("best member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(bestMember);

        for (int i = 1; i <= 20; i++) {

            BoardDto.BoardDtoBuilder builder = BoardDto.builder();

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

            em.persist(builder.build().toEntity());
        }

        em.flush();
        em.clear();

        // when
        BoardSearchCondition condition = BoardSearchCondition.builder()
                .title("test")
                .build();
        Pageable pageable = PageRequest.of(0, 2);

        Page<BoardNoPw> result = boardService.searchBoard(condition, pageable);

        // then
        long total = result.getTotalElements();
        List<BoardNoPw> content = result.getContent();

        assertThat(total).isEqualTo(10L);
        assertThat(content.size()).isEqualTo(2);
        assertThat(content)
                .extracting("title")
                .containsExactly("test title - 1", "test title - 3");
    }

    @Tag("searchBoard")
    @Test
    void 게시물_검색() throws Exception {
        // give
        Member testMember = MemberDto.builder()
                .username("test member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(testMember);

        Member bestMember = MemberDto.builder()
                .username("best member")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(bestMember);

        for (int i = 1; i <= 20; i++) {

            BoardDto.BoardDtoBuilder builder = BoardDto.builder();

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

            em.persist(builder.build().toEntity());
        }

        em.flush();
        em.clear();

        // when
        BoardSearchCondition condition = BoardSearchCondition.builder()
                .title("test")
                .nickname("test member")
                .content("1")
                .isAsc(false)
                .build();
        Pageable pageable = PageRequest.of(1, 3);

        Page<BoardNoPw> result = boardService.searchBoard(condition, pageable);

        // then
        long total = result.getTotalElements();
        List<BoardNoPw> content = result.getContent();

        for (BoardNoPw boardNoPw : content) {
            System.out.println("boardNoPw = " + boardNoPw);
        }

        assertThat(total).isEqualTo(6L);
        assertThat(content.size()).isEqualTo(3);
        assertThat(content)
                .extracting("content")
                .containsExactly("test content - 13", "test content - 11", "test content - 1");
    }

    @Tag("searchBoard")
    @Test
    void 게시물_검색_실패() throws Exception {
        // give
        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password("1234")
                .build();
        Board board = boardDto.toEntity();
        em.persist(board);

        em.flush();
        em.clear();

        // when
        BoardSearchCondition condition = BoardSearchCondition.builder()
                .title("best")
                .build();
        Pageable pageable = PageRequest.of(0, 20);
        // boardService.searchBoard(condition, pageable);

        // then
        assertThatThrownBy(() -> boardService.searchBoard(condition, pageable))
                .hasMessage("게시물이 없습니다.");
    }

}