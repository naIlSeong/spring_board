package toyproject.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.board.BoardDto;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
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

}