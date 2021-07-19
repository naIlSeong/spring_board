package toyproject.board.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<toyproject.board.domain.member.Member, Long> {
}
