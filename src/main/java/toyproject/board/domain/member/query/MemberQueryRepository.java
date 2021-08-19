package toyproject.board.domain.member.query;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.board.domain.member.Member;

public interface MemberQueryRepository extends JpaRepository<Member, Long>, MemberQueryRepositoryCustom {
}
