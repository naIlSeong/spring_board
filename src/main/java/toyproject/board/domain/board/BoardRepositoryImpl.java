package toyproject.board.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static toyproject.board.domain.board.QBoard.board;
import static toyproject.board.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteByMemberId(Long memberId) {
        queryFactory
                .delete(board)
                .where(board.member.id.eq(memberId))
                .execute();
    }

}
