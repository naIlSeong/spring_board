package toyproject.board.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQueryRepository extends JpaRepository<Member, Long>, MemberQueryRepositoryCustom {
}
