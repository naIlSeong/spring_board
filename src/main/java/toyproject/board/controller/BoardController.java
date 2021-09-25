package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.board.command.CreateBoardRequestDto;
import toyproject.board.dto.board.command.DeleteBoardRequestDto;
import toyproject.board.dto.board.command.UpdateBoardRequestDto;
import toyproject.board.dto.board.query.BoardAndCommentCount;
import toyproject.board.dto.board.query.BoardQueryDto;
import toyproject.board.dto.board.query.BoardSearchCondition;
import toyproject.board.dto.board.response.BoardDetailResponseDto;
import toyproject.board.dto.board.response.BoardListResponseDto;
import toyproject.board.dto.board.response.BoardResponseDto;
import toyproject.board.dto.comment.query.CommentQueryDto;
import toyproject.board.service.BoardService;
import toyproject.board.service.CommentService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @ResponseStatus(CREATED)
    @PostMapping("/new")
    public BoardResponseDto createBoard(@RequestBody CreateBoardRequestDto dto,
                                        @SessionAttribute(value = "member", required = false) Member member) {

        Long boardId = member != null
                ? boardService.createBoard(dto.toDto(member))
                : boardService.createBoard(dto.toDto());

        return BoardResponseDto.builder()
                .httpStatus(CREATED)
                .boardId(boardId)
                .build();
    }

    @PostMapping("/delete")
    public BasicResponseDto deleteBoard(@RequestBody DeleteBoardRequestDto dto,
                                        @SessionAttribute(value = "member", required = false) Member member) {

        boolean isLoggedIn = boardService.isLoggedIn(dto.getId());
        if (isLoggedIn) {
            if (member == null) {
                throw new IllegalArgumentException("로그인이 필요합니다.");
            }

            boardService.deleteBoard(dto.toDto(member)); // 로그인 메서드

        } else {
            boardService.deleteBoard(dto.toDto()); // 비로그인 메서드
        }

        return BasicResponseDto.builder()
                .httpStatus(OK)
                .build();
    }

    @PostMapping("/update")
    public BoardResponseDto updateBoard(@RequestBody UpdateBoardRequestDto dto,
                                        @SessionAttribute(value = "member", required = false) Member member) {

        boolean isLoggedIn = boardService.isLoggedIn(dto.getId());
        if (isLoggedIn) {
            if (member == null) {
                throw new IllegalArgumentException("로그인이 필요합니다.");
            }

            boardService.updateBoard(dto.toDto(member)); // 로그인 메서드

        } else {
            boardService.updateBoard(dto.toDto()); // 비로그인 메서드
        }

        return BoardResponseDto.builder()
                .httpStatus(OK)
                .boardId(dto.getId())
                .build();
    }

    @GetMapping("/{boardId}")
    public BoardDetailResponseDto getBoardDetail(@PathVariable("boardId") Long boardId, Pageable pageable) {

        BoardQueryDto board = boardService.getBoard(boardId);
        Page<CommentQueryDto> comments = commentService.getCommentsPage(boardId, pageable);

        return BoardDetailResponseDto.builder()
                .httpStatus(OK)
                .board(board)
                .comments(comments)
                .build();
    }

    @GetMapping("/list")
    public BoardListResponseDto getBoardList(Pageable pageable) {

        Page<BoardAndCommentCount> boardList = boardService.getBoardList(pageable);

        return BoardListResponseDto.builder()
                .httpStatus(OK)
                .boardList(boardList)
                .build();
    }

    @GetMapping("/search")
    public BoardListResponseDto searchBoardList(BoardSearchCondition condition, Pageable pageable) {

        Page<BoardAndCommentCount> boardList = boardService.searchBoard(condition, pageable);

        return BoardListResponseDto.builder()
                .httpStatus(OK)
                .boardList(boardList)
                .build();
    }

}
