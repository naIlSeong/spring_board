package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.board.BoardQueryRepository;
import toyproject.board.domain.board.BoardRepository;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.board.BoardDto;
import toyproject.board.dto.board.BoardNoPw;
import toyproject.board.dto.board.DeleteBoardDto;
import toyproject.board.dto.board.UpdateBoardDto;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;

    @Transactional
    public Long createBoard(BoardDto dto) {

        if (!isValid(dto)) {
            throw new IllegalArgumentException("제목과 내용은 필수입니다.");
        }

        // 비로그인 일 때
        if (dto.getMember() == null) {

            if (!isValidNoLogin(dto)) {
                throw new IllegalArgumentException("닉네임과 비밀번호는 필수입니다.");
            }

            if (dto.getNickname().length() < 2 || dto.getNickname().length() > 24) {
                throw new IllegalArgumentException("닉네임의 길이는 2자 이상, 24자 이하입니다.");
            }

            if (dto.getPassword().length() < 4) {
                throw new IllegalArgumentException("비밀번호의 길이는 4자 이상입니다.");
            }

            dto.setPassword(
                    BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10))
            );
        } else {
            // 로그인 일 때, 닉네임 패스워드가 넘와 올 경우
            // 닉네임은 회원의 username
            dto.setNickname(dto.getMember().getUsername());
            dto.setPassword(null);
        }

        Board board = dto.toEntity();
        boardRepository.save(board);

        return board.getId();
    }

    private boolean isValidNoLogin(BoardDto dto) {
        return hasText(dto.getNickname()) && hasText(dto.getPassword());
    }

    private boolean isValid(BoardDto dto) {
        return hasText(dto.getTitle()) && hasText(dto.getContent());
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
            if (member == null || board.getMember().getId() != member.getId()) {
                throw new IllegalArgumentException("게시물을 삭제할 수 없습니다.");
            }

        } else { // 비로그인
            boolean isMatch = BCrypt.checkpw(password, board.getPassword());
            if (!isMatch) {
                throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
            }
        }

    }

    @Transactional(readOnly = true)
    public BoardNoPw getBoard(Long boardId) {

        BoardNoPw board = boardQueryRepository.findNoPasswordById(boardId);
        if (board == null) {
            throw new NullPointerException("게시물을 찾을 수 없습니다.");
        }

        return board;
    }

    @Transactional(readOnly = true)
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

}
