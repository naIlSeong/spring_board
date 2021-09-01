package toyproject.board.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<toyproject.board.domain.board.Board, Long>, BoardRepositoryCustom {
}
