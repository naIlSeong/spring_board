package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.board.BoardRepository;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.comment.CommentRepository;
import toyproject.board.dto.comment.CreateCommentLoginDto;
import toyproject.board.dto.comment.CreateCommentNotLoginDto;
import toyproject.board.dto.comment.UpdateCommentLoginDto;
import toyproject.board.dto.comment.UpdateCommentNotLoginDto;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    @Validated
    public Long createComment(@Valid CreateCommentLoginDto dto) {

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Comment comment = dto.toEntity(board);
        commentRepository.save(comment);

        return comment.getId();
    }

    @Transactional
    @Validated
    public Long createComment(@Valid CreateCommentNotLoginDto dto) {

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        dto.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10)));

        Comment comment = dto.toEntity(board);
        commentRepository.save(comment);

        return comment.getId();
    }

    @Transactional
    @Validated
    public Long updateComment(@Valid UpdateCommentLoginDto dto) {

        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Long memberId = comment.getMember().getId();
        Long requestMemberId = dto.getMember().getId();
        if (!memberId.equals(requestMemberId)) {
            throw new IllegalArgumentException("댓글을 수정할 수 없습니다.");
        }

        comment.updateContent(dto.getContent());

        return comment.getId();
    }

    @Transactional
    @Validated
    public Long updateComment(@Valid UpdateCommentNotLoginDto dto) {

        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        String hashed = comment.getPassword();
        String plainPassword = dto.getPassword();
        boolean isMatch = BCrypt.checkpw(plainPassword, hashed);
        if (!isMatch) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
        }

        comment.updateContent(dto.getContent());

        return comment.getId();
    }

}
