package toyproject.board.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<toyproject.board.domain.member.Member, Long> {

    Optional<Member> findByUsername(String username);

}
