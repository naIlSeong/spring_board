package toyproject.board.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    @Override
    public void deleteByMemberId(Long memberId) {

        List<Long> boardIdList = queryFactory
                .select(board.id)
                .from(board)
                .where(board.member.id.eq(memberId))
                .fetch();

        queryFactory
                .delete(comment)
                .where(comment.board.id.in(boardIdList))
                .execute();

        /*
        queryFactory
                .delete(comment)
                .where(comment.board.id.in(
                        queryFactory
                                .select(board.id)
                                .where(board.member.id.eq(memberId))
                                .fetch()
                ))
                .execute();
         */

        queryFactory
                .delete(board)
                .where(board.member.id.eq(memberId))
                .execute();
    }

}
