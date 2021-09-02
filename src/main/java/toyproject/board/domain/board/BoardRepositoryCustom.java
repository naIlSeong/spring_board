package toyproject.board.domain.board;

public interface BoardRepositoryCustom {

    void deleteWithComments(Long boardId);

    void deleteByMemberId(Long memberId);

}
