package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.board.BoardQueryRepository;
import toyproject.board.domain.board.BoardRepository;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.board.*;

import javax.validation.Valid;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Validated
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;

    @Transactional
    @Validated
    public Long createBoard(@Valid CreateBoardLoginDto dto) {

        Member member = dto.getMember();
        dto.setNickname(member.getUsername());

        Board board = dto.toEntity();
        boardRepository.save(board);

        return board.getId();
    }

    @Transactional
    @Validated
    public Long createBoard(@Valid CreateBoardNotLoginDto dto) {

        dto.setPassword(
                BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10))
        );

        Board board = dto.toEntity();
        boardRepository.save(board);

        return board.getId();
    }

    @Transactional
    @Validated // 로그인
    public void deleteBoard(@Valid DeleteBoardLoginDto dto) {

        Board board = getBoardWithPassword(dto.getId());
        checkMemberId(board.getMember().getId(), dto.getMember().getId(), true);

        boardRepository.delete(board);

    }

    @Transactional
    @Validated // 비로그인
    public void deleteBoard(@Valid DeleteBoardNotLoginDto dto) {

        Board board = getBoardWithPassword(dto.getId());
        checkPassword(dto.getPassword(), board.getPassword());

        boardRepository.delete(board);

    }

    @Transactional
    @Validated // 로그인
    public void updateBoard(@Valid UpdateBoardLoginDto dto) {

        if (hasText(dto.getTitle()) || hasText(dto.getContent())) {
            Board board = getBoardWithPassword(dto.getId());
            checkMemberId(board.getMember().getId(), dto.getMember().getId(), false);
            updateTitleAndContent(dto.getTitle(), dto.getContent(), board);
        }

    }

    @Transactional
    @Validated // 비로그인
    public void updateBoard(@Valid UpdateBoardNotLoginDto dto) {

        if (hasText(dto.getTitle()) || hasText(dto.getContent())) {
            Board board = getBoardWithPassword(dto.getId());
            checkPassword(dto.getPassword(), board.getPassword());
            updateTitleAndContent(dto.getTitle(), dto.getContent(), board);
        }

    }

    private void checkMemberId(Long memberId, Long requestMemberId, boolean isDelete) {
        if (!memberId.equals(requestMemberId)) {
            String condition = isDelete ? "삭제" : "수정";
            String message = "게시물을 " + condition + "할 수 없습니다.";
            throw new IllegalArgumentException(message);
        }
    }

    private void checkPassword(String plainPassword, String hashed) {
        boolean isMatch = BCrypt.checkpw(plainPassword, hashed);
        if (!isMatch) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
        }
    }

    /**
     * SELECT * FROM board WHERE baord.board_id = :boardId
     */
    public Board getBoardWithPassword(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException("게시물을 찾을 수 없습니다."));
    }

    /**
     * SELECT board.password FROM board WHERE board.board_id = :boardId
     */
    public boolean isLoggedIn(Long boardId) {
        CheckPasswordDto result = boardQueryRepository.findPassword(boardId);
        if (result == null) {
            throw new NullPointerException("게시물을 찾을 수 없습니다.");
        }
        return result.getPassword() == null;
    }

    private void updateTitleAndContent(String title, String content, Board board) {

        if (hasText(title)) {
            board.updateTitle(title);
        }

        if (hasText(content)) {
            board.updateContent(content);
        }

    }

    public BoardNoPw getBoard(Long boardId) {

        BoardNoPw board = boardQueryRepository.findNoPasswordById(boardId);
        if (board == null) {
            throw new NullPointerException("게시물을 찾을 수 없습니다.");
        }

        return board;
    }

    public Page<BoardNoPw> getBoardList(Pageable pageable, Long memberId) {

        Page<BoardNoPw> result = boardQueryRepository.findAllNoPassword(memberId, pageable);

        if (result.getTotalElements() == 0) {
            throw new NullPointerException("게시물이 없습니다.");
        }

        if (pageable.getPageNumber() >= result.getTotalPages()) {
            Pageable newPageable = PageRequest.of(result.getTotalPages() - 1, result.getSize());
            return boardQueryRepository.findAllNoPassword(memberId, newPageable);
        }

        return result;
    }

    public Page<BoardNoPw> searchBoard(BoardSearchCondition condition, Pageable pageable) {

        Page<BoardNoPw> result = boardQueryRepository.searchBoard(condition, pageable);

        if (result.getTotalElements() == 0) {
            throw new NullPointerException("게시물이 없습니다.");
        }

        if (pageable.getPageNumber() >= result.getTotalPages()) {
            Pageable newPageable = PageRequest.of(result.getTotalPages() - 1, result.getSize());
            return boardQueryRepository.searchBoard(condition, newPageable);
        }

        return result;
    }

}
