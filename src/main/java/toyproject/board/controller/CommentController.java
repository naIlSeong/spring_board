package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.comment.CommentResponseDto;
import toyproject.board.dto.comment.CreateCommentRequestDto;
import toyproject.board.service.CommentService;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(CREATED)
    @PostMapping("/new")
    public CommentResponseDto createComment(@RequestBody CreateCommentRequestDto dto,
                                            HttpSession session) {

        Long commentId;
        Member member = (Member) session.getAttribute("member");

        if (member != null) {

            dto.setMember(member);
            commentId = commentService.createComment(dto.toDto(member));

        } else {
            commentId = commentService.createComment(dto.toDto());
        }


        return CommentResponseDto.builder()
                .httpStatus(CREATED)
                .commentId(commentId)
                .build();
    }

}
