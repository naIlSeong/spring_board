package toyproject.board.domain.board.query;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.board.domain.board.Board;

public interface BoardQueryRepository extends JpaRepository<Board, Long>, BoardQueryRepositoryCustom {
}
