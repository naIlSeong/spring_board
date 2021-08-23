package toyproject.board.domain.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import toyproject.board.dto.comment.query.CommentQueryDto;
import toyproject.board.dto.comment.QCommentNoPw;

import java.util.List;

import static toyproject.board.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentQueryDto> getCommentsByBoardId(Long boardId) {
        return queryFactory
                .select(new QCommentNoPw(
                        comment.id,
                        comment.content,
                        comment.nickname,
                        comment.member.id,
                        comment.board.id,
                        comment.createdDate,
                        comment.lastModifiedDate
                ))
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .fetch();
    }

}
