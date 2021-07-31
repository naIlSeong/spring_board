package toyproject.board.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import toyproject.board.dto.board.BoardNoPw;
import toyproject.board.dto.board.QBoardNoPw;

import static toyproject.board.domain.board.QBoard.board;

@RequiredArgsConstructor
public class BoardQueryRepositoryImpl implements BoardQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public BoardNoPw findNoPasswordById(Long boardId) {
        return queryFactory
                .select(new QBoardNoPw(
                        board.id,
                        board.title,
                        board.content,
                        board.nickname,
                        board.member.id,
                        board.createdDate,
                        board.lastModifiedDate
                ))
                .from(board)
                .where(board.id.eq(boardId))
                .fetchOne();
    }

}
