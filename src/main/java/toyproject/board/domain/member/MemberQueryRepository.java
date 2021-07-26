package toyproject.board.domain.member;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.board.dto.member.MemberNoPw;
import toyproject.board.dto.member.MemberSearchCondition;
import toyproject.board.dto.member.QMemberNoPw;

import java.util.List;

import static toyproject.board.domain.member.QMember.member;


@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * password만 제외한 Member 객체
     */
    public MemberNoPw findById(Long memberId) {
        return queryFactory
                .select(new QMemberNoPw(
                        member.id,
                        member.username,
                        member.createdDate,
                        member.lastModifiedDate
                ))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();
    }

    public List<MemberNoPw> searchMember(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberNoPw(
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

}
