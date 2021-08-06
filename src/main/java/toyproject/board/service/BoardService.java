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
    @Validated({NotLogin.class, Default.class})
    public Long createBoardNotLogin(@Valid BoardDto dto) {

        dto.setPassword(
                BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10))
        );

        Board board = dto.toEntity();
        boardRepository.save(board);

        return board.getId();
    }

    @Transactional
    @Validated({Login.class, Default.class})
    public Long createBoardLogin(@Valid BoardDto dto) {

        dto.setNickname(dto.getMember().getUsername());
        dto.setPassword(null);

        Board board = dto.toEntity();
        boardRepository.save(board);

        return board.getId();
    }

    @Transactional
    public boolean deleteBoard(DeleteBoardDto dto) {

        Board board = boardRepository.findById(dto.getId())
                .orElseThrow(() -> new NullPointerException("게시물을 찾을 수 없습니다."));
        isDeletable(board, dto.getMember(), dto.getPassword());

        boardRepository.delete(board);

        return true;
    }

    @Transactional
    public Long updateBoard(UpdateBoardDto dto) {

        Board board = boardRepository.findById(dto.getId())
                .orElseThrow(() -> new NullPointerException("게시물을 찾을 수 없습니다."));
        isDeletable(board, dto.getMember(), dto.getPassword());

        if (hasText(dto.getTitle())) {
            board.updateTitle(dto.getTitle());
        }
        if (hasText(dto.getContent())) {
            board.updateContent(dto.getContent());
        }

        return board.getId();
    }

    private void isDeletable(Board board, Member member, String password) {

        if (board.getMember() != null) { // 로그인 필요
            if (member == null || !board.getMember().getId().equals(member.getId())) {
                throw new IllegalArgumentException("게시물을 삭제할 수 없습니다.");
            }

        } else { // 비로그인
            boolean isMatch = BCrypt.checkpw(password, board.getPassword());
            if (!isMatch) {
                throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
            }
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
