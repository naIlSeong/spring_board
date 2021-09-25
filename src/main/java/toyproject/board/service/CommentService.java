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
import toyproject.board.domain.board.BoardRepository;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.comment.CommentRepository;
import toyproject.board.dto.CheckPasswordDto;
import toyproject.board.dto.comment.command.*;
import toyproject.board.dto.comment.query.CommentQueryDto;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 생성 (로그인)
    @Transactional
    @Validated
    public Long createComment(@Valid CreateCommentLoginDto dto) {

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Comment comment = dto.toEntity(board);
        commentRepository.save(comment);

        return comment.getId();
    }

    // 댓글 생성 (비로그인)
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

    // 댓글 수정 (로그인)
    @Transactional
    @Validated
    public Long updateComment(@Valid UpdateCommentLoginDto dto) {

        Comment comment = getComment(dto.getCommentId());

        Long memberId = comment.getMember().getId();
        Long requestMemberId = dto.getMember().getId();
        if (!memberId.equals(requestMemberId)) {
            throw new IllegalArgumentException("댓글을 수정할 수 없습니다.");
        }

        comment.updateContent(dto.getContent());

        return comment.getId();
    }

    // 댓글 수정 (비로그인)
    @Transactional
    @Validated
    public Long updateComment(@Valid UpdateCommentNotLoginDto dto) {

        Comment comment = getComment(dto.getCommentId());

        String hashed = comment.getPassword();
        String plainPassword = dto.getPassword();
        boolean isMatch = BCrypt.checkpw(plainPassword, hashed);
        if (!isMatch) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
        }

        comment.updateContent(dto.getContent());

        return comment.getId();
    }

    // PK로 댓글 조회
    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    // PK로 비밀번호 컬럼 조회
    public boolean isLoggedId(Long commentId) {
        CheckPasswordDto result = commentRepository.getPassword(commentId);
        if (result == null) {
            throw new NullPointerException("댓글을 찾을 수 없습니다.");
        }
        return result.getPassword() == null;
    }

    // 댓글 삭제 (로그인)
    @Transactional
    @Validated
    public void deleteComment(@Valid DeleteCommentLoginDto dto) {

        Comment comment = getComment(dto.getId());

        Long memberId = comment.getMember().getId();
        Long requestMemberId = dto.getMember().getId();
        if (!memberId.equals(requestMemberId)) {
            throw new IllegalArgumentException("댓글을 삭제할 수 없습니다.");
        }

        commentRepository.delete(comment);
    }

    // 댓글 삭제 (비로그인)
    @Transactional
    @Validated
    public void deleteComment(@Valid DeleteCommentNotLoginDto dto) {

        Comment comment = getComment(dto.getId());

        String hashed = comment.getPassword();
        String plainPassword = dto.getPassword();
        boolean isMatch = BCrypt.checkpw(plainPassword, hashed);
        if (!isMatch) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
        }

        commentRepository.delete(comment);
    }

    // 게시물에 달린 댓글 조회 페이징
    public Page<CommentQueryDto> getCommentsPage(Long boardId, Pageable pageable) {

        Page<CommentQueryDto> result = commentRepository.getCommentsPageByBoardId(boardId, pageable);

        // 마지막 페이지보다 큰 페이지번호로 요청하면 마지막 페이지를 반환
        if (result.getTotalElements() != 0 && pageable.getPageNumber() >= result.getTotalPages()) {
            PageRequest newPageable = PageRequest.of(result.getTotalPages() - 1, pageable.getPageSize());
            return commentRepository.getCommentsPageByBoardId(boardId, newPageable);
        }

        return result;
    }

}
