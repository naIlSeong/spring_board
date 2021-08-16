package toyproject.board.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<toyproject.board.domain.comment.Comment, Long>, CommentRepositoryCustom {
}
