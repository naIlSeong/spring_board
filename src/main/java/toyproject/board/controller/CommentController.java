package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.comment.CommentResponseDto;
import toyproject.board.dto.comment.CreateCommentDto;
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
    public CommentResponseDto createComment(@RequestBody CreateCommentDto dto,
                                            HttpSession session) {

        Long commentId;

        Object attribute = session.getAttribute("member");
        if (attribute != null) {
            Member member = (Member) attribute;
            dto.setMember(member);

            commentId = commentService.createCommentLogin(dto);

        } else {
            commentId = commentService.createCommentNotLogin(dto);
        }

        return CommentResponseDto.builder()
                .httpStatus(CREATED)
                .commentId(commentId)
                .build();
    }

}
