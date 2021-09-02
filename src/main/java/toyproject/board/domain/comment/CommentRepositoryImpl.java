package toyproject.board.domain.comment;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import toyproject.board.dto.board.query.CheckPasswordDto;
import toyproject.board.dto.board.query.QCheckPasswordDto;
import toyproject.board.dto.comment.query.CommentQueryDto;
import toyproject.board.dto.comment.query.QCommentQueryDto;

import java.util.List;

import static toyproject.board.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentQueryDto> getCommentsByBoardId(Long boardId) {
        return queryFactory
                .select(new QCommentQueryDto(
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

    @Override
    public Page<CommentQueryDto> getCommentsPageByBoardId(Long boardId, Pageable pageable) {
        QueryResults<CommentQueryDto> results = queryFactory
                .select(new QCommentQueryDto(
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.id.asc())
                .fetchResults();

        List<CommentQueryDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public CheckPasswordDto getPassword(Long id) {
        return queryFactory
                .select(new QCheckPasswordDto(
                        comment.id,
                        comment.password
                ))
                .from(comment)
                .where(comment.id.eq(id))
                .fetchOne();
    }

    @Override
    public void deleteByBoardId(Long boardId) {
        queryFactory
                .delete(comment)
                .where(comment.board.id.eq(boardId))
                .execute();
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        queryFactory
                .delete(comment)
                .where(comment.member.id.eq(memberId))
                .execute();
    }

}
