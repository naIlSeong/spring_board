package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.comment.command.CreateCommentRequestDto;
import toyproject.board.dto.comment.command.DeleteCommentRequestDto;
import toyproject.board.dto.comment.command.UpdateCommentRequestDto;
import toyproject.board.dto.comment.response.CommentResponseDto;
import toyproject.board.service.CommentService;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(CREATED)
    @PostMapping("/new")
    public CommentResponseDto createComment(@RequestBody CreateCommentRequestDto dto,
                                            @SessionAttribute(value = "member", required = false) Member member) {

        Long commentId = member != null
                ? commentService.createComment(dto.toDto(member))
                : commentService.createComment(dto.toDto());

        return CommentResponseDto.builder()
                .httpStatus(CREATED)
                .commentId(commentId)
                .build();
    }

    @PostMapping("/update")
    public CommentResponseDto updateComment(@RequestBody UpdateCommentRequestDto dto,
                                            @SessionAttribute(value = "member", required = false) Member member) {

        Long commentId;

        boolean isLoggedIn = commentService.isLoggedId(dto.getCommentId());
        if (isLoggedIn) {
            if (member == null) {
                throw new IllegalArgumentException("로그인이 필요합니다.");
            }

            commentId = commentService.updateComment(dto.toDto(member)); // 로그인 메서드

        } else {
            commentId = commentService.updateComment(dto.toDto()); // 비로그인 메서드
        }

        return CommentResponseDto.builder()
                .httpStatus(OK)
                .commentId(commentId)
                .build();
    }

    @PostMapping("/delete")
    public BasicResponseDto deleteComment(@RequestBody DeleteCommentRequestDto dto,
                                          @SessionAttribute(value = "member", required = false) Member member) {

        boolean isLoggedIn = commentService.isLoggedId(dto.getId());
        if (isLoggedIn) {
            if (member == null) {
                throw new IllegalArgumentException("로그인이 필요합니다.");
            }

            commentService.deleteComment(dto.toDto(member)); // 로그인 메서드

        } else {
            commentService.deleteComment(dto.toDto()); // 비로그인 메서드
        }

        return BasicResponseDto.builder()
                .httpStatus(OK)
                .build();
    }

}
