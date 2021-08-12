package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.board.*;
import toyproject.board.service.BoardService;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @ResponseStatus(CREATED)
    @PostMapping("/new")
    public BoardResponseDto createBoard(@RequestBody CreateBoardRequestDto dto,
                                        HttpSession session) {

        Member member = (Member) session.getAttribute("member");
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
                                        HttpSession session) {

        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            boardService.deleteBoard(dto.toDto(member));
        } else {
            boardService.deleteBoard(dto.toDto());
        }

        return BasicResponseDto.builder()
                .httpStatus(OK)
                .build();
    }

    @PostMapping("/update")
    public BoardResponseDto updateBoard(@RequestBody UpdateBoardRequestDto dto,
                                        HttpSession session) {

        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            boardService.updateBoard(dto.toDto(member));
        } else {
            boardService.updateBoard(dto.toDto());
        }

        return BoardResponseDto.builder()
                .httpStatus(OK)
                .boardId(dto.getId())
                .build();
    }

    @GetMapping("/{id}")
    public BoardQueryResponseDto getBoard(@PathVariable("id") Long id) {

        BoardNoPw board = boardService.getBoard(id);

        return BoardQueryResponseDto.builder()
                .httpStatus(OK)
                .boardNoPw(board)
                .build();
    }

    @GetMapping("/list")
    public BoardListResponseDto getList(Pageable pageable,
                                        @RequestParam(name = "memberId", required = false) Long memberId) {

        Page<BoardNoPw> boardList = boardService.getBoardList(pageable, memberId);

        return BoardListResponseDto.builder()
                .httpStatus(OK)
                .boardList(boardList)
                .build();
    }

    @GetMapping("/search")
    public BoardListResponseDto searchBoard(Pageable pageable,
                                            BoardSearchCondition condition) {

        Page<BoardNoPw> boardList = boardService.searchBoard(condition, pageable);

        return BoardListResponseDto.builder()
                .httpStatus(OK)
                .boardList(boardList)
                .build();
    }

}
