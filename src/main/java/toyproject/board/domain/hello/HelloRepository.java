package toyproject.board.domain.hello;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<Hello, Long>, HelloRepositoryCustom {
}
