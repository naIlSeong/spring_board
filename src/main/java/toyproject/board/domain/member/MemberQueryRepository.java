package toyproject.board.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.board.dto.MemberNoPw;
import toyproject.board.dto.QMemberNoPw;

import static toyproject.board.domain.member.QMember.*;

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

}
