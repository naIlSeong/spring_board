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
import toyproject.board.marker.Login;
import toyproject.board.marker.NotLogin;

import javax.validation.Valid;
import javax.validation.groups.Default;

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
    @Validated({NotLogin.class, Default.class})
    public void deleteBoardNotLogin(@Valid DeleteBoardDto dto) {

        Board board = isWriteableNotLogin(dto.getId(), dto.getPassword());

        boardRepository.delete(board);

    }

    @Transactional
    @Validated(Login.class)
    public void deleteBoardLogin(@Valid DeleteBoardDto dto) {

        Board board = isWriteableLogin(dto.getId(), dto.getMember());

        boardRepository.delete(board);

    }

    @Transactional
    @Validated({NotLogin.class, Default.class})
    public void updateBoardNotLogin(@Valid UpdateBoardDto dto) {

        Board board = isWriteableNotLogin(dto.getId(), dto.getPassword());

        updateTitleAndContent(dto.getTitle(), dto.getContent(), board);

    }

    @Transactional
    @Validated({Login.class, Default.class})
    public void updateBoardLogin(@Valid UpdateBoardDto dto) {

        Board board = isWriteableLogin(dto.getId(), dto.getMember());

        updateTitleAndContent(dto.getTitle(), dto.getContent(), board);

    }

    private void updateTitleAndContent(String title, String content, Board board) {

        if (hasText(title)) {
            board.updateTitle(title);
        }

        if (hasText(content)) {
            board.updateContent(content);
        }

    }

    private Board isWriteableNotLogin(Long boardId, String password) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException("게시물을 찾을 수 없습니다."));

        boolean isMatch = BCrypt.checkpw(password, board.getPassword());
        if (!isMatch) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
        }

        return board;
    }

    private Board isWriteableLogin(Long boardId, Member member) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException("게시물을 찾을 수 없습니다."));

        Long memberId = board.getMember().getId();
        Long requestMemberId = member.getId();

        if (!memberId.equals(requestMemberId)) {
            throw new IllegalArgumentException("게시물을 삭제할 수 없습니다.");
        }

        return board;
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
