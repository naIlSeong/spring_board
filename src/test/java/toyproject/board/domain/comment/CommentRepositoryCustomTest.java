package toyproject.board.domain.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.board.Board;
import toyproject.board.dto.comment.query.CommentQueryDto;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class CommentRepositoryCustomTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityManager em;

    private Long boardId;

    @BeforeEach
    void beforeEach() {

        Board board = Board.builder()
                .title("board title")
                .content("board content")
                .nickname("board creator")
                .password("1234")
                .build();
        em.persist(board);
        boardId = board.getId();

        for (int i = 0; i < 20; i++) {
            Comment comment = Comment.builder()
                    .board(board)
                    .content("comment " + i)
                    .nickname("test " + i)
                    .password("1234")
                    .build();
            em.persist(comment);
        }

    }

    @Test
    void 댓글_리스트_쿼리() throws Exception {
        // give
        
        // when
        List<CommentQueryDto> list = commentRepository.getCommentsByBoardId(boardId);

        // then
        for (CommentQueryDto commentNoPw : list) {
            System.out.println("commentNoPw = " + commentNoPw);
        }
    }

}