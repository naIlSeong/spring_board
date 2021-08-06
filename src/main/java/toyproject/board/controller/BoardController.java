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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/new")
    public BoardResponseDto createBoard(@RequestBody BoardDto dto,
                                        HttpSession session) {

        Long boardId;

        Object attribute = session.getAttribute("member");
        if (attribute != null) {
            Member member = (Member) attribute;
            dto.setMember(member);

            boardId = boardService.createBoardLogin(dto);

        } else {
            boardId = boardService.createBoardNotLogin(dto);
        }

        return BoardResponseDto.builder()
                .httpStatus(CREATED)
                .boardId(boardId)
                .build();
    }

    @PostMapping("/delete")
    public BasicResponseDto deleteBoard(@RequestBody DeleteBoardDto dto,
                                        HttpSession session) {

        Object attribute = session.getAttribute("member");
        if (attribute != null) {
            Member member = (Member) attribute;
            dto.setMember(member);

            boardService.deleteBoardLogin(dto);

        } else {
            boardService.deleteBoardNotLogin(dto);
        }

        return BasicResponseDto.builder()
                .httpStatus(OK)
                .build();
    }

    @PostMapping("/update")
    public BoardResponseDto updateBoard(@RequestBody UpdateBoardDto dto,
                                        HttpSession session) {

        Object attribute = session.getAttribute("member");
        if (attribute != null) {
            Member member = (Member) attribute;
            dto.setMember(member);

            boardService.updateBoardLogin(dto);

        } else {
            boardService.updateBoardNotLogin(dto);
        }

        return BoardResponseDto.builder()
                .httpStatus(OK)
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
