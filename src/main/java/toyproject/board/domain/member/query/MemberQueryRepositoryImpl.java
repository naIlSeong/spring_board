package toyproject.board.domain.member.query;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import toyproject.board.dto.member.query.MemberQueryDto;
import toyproject.board.dto.member.query.MemberSearchCondition;
import toyproject.board.dto.member.query.QMemberQueryDto;

import java.util.List;

import static toyproject.board.domain.member.QMember.member;

@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberQueryDto findNoPasswordById(Long memberId) {
        return queryFactory
                .select(new QMemberQueryDto(
                        member.id,
                        member.username,
                        member.createdDate,
                        member.lastModifiedDate
                ))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();


    }

    @Override
    public List<MemberQueryDto> searchMember(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberQueryDto(
                        member.id,
                        member.username,
                        member.createdDate,
                        member.lastModifiedDate
                ))
                .from(member)
                .where(usernameLike(condition))
                .orderBy(member.id.asc())
                .fetch();
    }

    private Predicate usernameLike(MemberSearchCondition condition) {
        return condition.getUsername() != null
                ? member.username.like("%" + condition.getUsername() + "%")
                : null;
    }


    @Override
    public Page<MemberQueryDto> searchPage(MemberSearchCondition condition, Pageable pageable) {

        QueryResults<MemberQueryDto> results = queryFactory
                .select(new QMemberQueryDto(
                        member.id,
                        member.username,
                        member.createdDate,
                        member.lastModifiedDate
                ))
                .from(member)
                .where(usernameLike(condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.id.asc())
                .fetchResults();

        List<MemberQueryDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);

    }
}
