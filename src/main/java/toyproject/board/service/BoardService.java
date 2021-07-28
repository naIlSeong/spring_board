package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.board.BoardRepository;
import toyproject.board.domain.member.MemberRepository;
import toyproject.board.dto.board.BoardDto;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

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

}
