package toyproject.board.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static toyproject.board.domain.board.QBoard.board;
import static toyproject.board.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * Cascade
     */
    @Override
    public void deleteWithComments(Long boardId) {

        queryFactory
                .delete(comment)
                .where(comment.board.id.eq(boardId))
                .execute();

        queryFactory
                .delete(board)
                .where(board.id.eq(boardId))
                .execute();

    }

}
