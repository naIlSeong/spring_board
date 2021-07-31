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
import toyproject.board.dto.board.BoardDto;
import toyproject.board.dto.board.BoardNoPw;
import toyproject.board.dto.board.DeleteBoardDto;
import toyproject.board.dto.board.UpdateBoardDto;
import toyproject.board.dto.member.MemberDto;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

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
        BoardDto dto = BoardDto.builder()
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

    @Tag("createBoard")
    @Test
    void 게시물_작성_실퍠_제목() throws Exception {
        // give
        BoardDto dto = BoardDto.builder()
                .content("test content.")
                .nickname("test")
                .password("1234")
                .build();

        // when
        // boardService.createBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.createBoard(dto))
                .hasMessage("제목과 내용은 필수입니다.");

    }

    @Tag("createBoard")
    @Test
    void 게시물_작성_실퍠_내용() throws Exception {
        // give
        BoardDto dto = BoardDto.builder()
                .title("test title.")
                .nickname("test")
                .password("1234")
                .build();

        // when
        // boardService.createBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.createBoard(dto))
                .hasMessage("제목과 내용은 필수입니다.");

    }

    @Tag("createBoard")
    @Test
    void 게시물_작성_실퍠_닉네임() throws Exception {
        // give
        BoardDto dto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .password("1234")
                .build();

        // when
        // boardService.createBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.createBoard(dto))
                .hasMessage("닉네임과 비밀번호는 필수입니다.");

    }

    @Tag("createBoard")
    @Test
    void 게시물_작성_실퍠_비밀번호() throws Exception {
        // give
        BoardDto dto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .build();

        // when
        // boardService.createBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.createBoard(dto))
                .hasMessage("닉네임과 비밀번호는 필수입니다.");

    }

    @Tag("createBoard")
    @Test
    void 게시물_작성_실퍠_닉네임길이1() throws Exception {
        // give
        BoardDto dto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("t")
                .password("1234")
                .build();

        // when
        // boardService.createBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.createBoard(dto))
                .hasMessage("닉네임의 길이는 2자 이상, 24자 이하입니다.");

    }

    @Tag("createBoard")
    @Test
    void 게시물_작성_실퍠_닉네임길이2() throws Exception {
        // give
        BoardDto dto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test1234test1234test123499")
                .password("1234")
                .build();

        // when
        // boardService.createBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.createBoard(dto))
                .hasMessage("닉네임의 길이는 2자 이상, 24자 이하입니다.");

    }

    @Tag("createBoard")
    @Test
    void 게시물_작성_실퍠_비밀번호길이() throws Exception {
        // give
        BoardDto dto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .nickname("test")
                .password("123")
                .build();

        // when
        // boardService.createBoard(dto);

        // then
        assertThatThrownBy(() -> boardService.createBoard(dto))
                .hasMessage("비밀번호의 길이는 4자 이상입니다.");

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
        Long boardId = boardService.createBoard(boardDto);

        em.flush();
        em.clear();

        // when
        DeleteBoardDto dto = DeleteBoardDto.builder()
                .id(boardId)
                .member(member)
                .build();
        boolean result = boardService.deleteBoard(dto);

        // then
        assertThat(result).isTrue();

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
        Long boardId = boardService.createBoard(boardDto);

        // when
        DeleteBoardDto dto = DeleteBoardDto.builder()
                .id(boardId)
                .password("1234")
                .build();
        boolean result = boardService.deleteBoard(dto);

        // then
        assertThat(result).isTrue();

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

        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        Long boardId = boardService.createBoard(boardDto);

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

        assertThatThrownBy(() -> boardService.deleteBoard(dto))
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
        Long boardId = boardService.createBoard(boardDto);

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

        assertThatThrownBy(() -> boardService.deleteBoard(dto))
                .hasMessage("비밀번호를 다시 확인해 주세요.");
        assertThat(BCrypt.checkpw("6789", board.getPassword())).isFalse();
    }

    @Tag("deleteBoard")
    @Test
    void 게시물_삭제_실패_로그인필요() throws Exception {
        // give
        Member member = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build()
                .toEntity();
        em.persist(member);

        BoardDto boardDto = BoardDto.builder()
                .title("test title.")
                .content("test content.")
                .member(member)
                .build();
        Long boardId = boardService.createBoard(boardDto);

        em.flush();
        em.clear();

        // when
        DeleteBoardDto dto = DeleteBoardDto.builder()
                .id(boardId)
                .build();
        // boardService.deleteBoard(dto);

        // then
        Board board = em.find(Board.class, boardId);

        assertThatThrownBy(() -> boardService.deleteBoard(dto))
                .hasMessage("게시물을 삭제할 수 없습니다.");
        assertThat(board.getMember().getId()).isEqualTo(member.getId());
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
        Long boardId = boardService.createBoard(boardDto);

        // when
        UpdateBoardDto dto = UpdateBoardDto.builder()
                .id(boardId)
                .member(member)
                .title("updated title.")
                .content("updated content.")
                .build();
        Long result = boardService.updateBoard(dto);

        em.flush();
        em.clear();

        // then
        Board board = em.find(Board.class, boardId);

        assertThat(result).isEqualTo(boardId);
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
        Long boardId = boardService.createBoard(boardDto);

        // when
        UpdateBoardDto dto = UpdateBoardDto.builder()
                .id(boardId)
                .password("1234")
                .title("updated title.")
                .content("updated content.")
                .build();
        Long result = boardService.updateBoard(dto);

        em.flush();
        em.clear();

        // then
        Board board = em.find(Board.class, boardId);

        assertThat(result).isEqualTo(boardId);
        assertThat(board.getTitle()).isEqualTo("updated title.");
        assertThat(board.getContent()).isEqualTo("updated content.");
        assertThat(board.getMember()).isNull();
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
        Long boardId = boardService.createBoard(boardDto);

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
        Page<BoardNoPw> result = boardService.getBoardList(pageable);

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
        Page<BoardNoPw> result = boardService.getBoardList(pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(100);
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(4);
        assertThat(result.isLast()).isTrue();
        assertThat(result.getContent().size()).isEqualTo(20);
        assertThat(result.getContent().get(19).getTitle()).isEqualTo("title99");
    }

}