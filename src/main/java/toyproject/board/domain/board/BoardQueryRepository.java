package toyproject.board.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardQueryRepository extends JpaRepository<Board, Long>, BoardQueryRepositoryCustom {
}
