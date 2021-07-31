package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.board.*;
import toyproject.board.service.BoardService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board/new")
    public BoardResponseDto createBoard(
            @RequestBody BoardDto dto,
            HttpSession session,
            HttpServletResponse response) {

        BoardResponseDto responseDto = BoardResponseDto.builder()
                .httpStatus(CREATED)
                .build();

        try {
            Object attribute = session.getAttribute("member");
            if (attribute != null) {
                Member member = (Member) attribute;
                dto.setMember(member);
            }

            Long boardId = boardService.createBoard(dto);

            responseDto.setBoardId(boardId);
            response.setStatus(SC_CREATED);

        } catch (IllegalArgumentException e) {
            responseDto.setHttpStatus(BAD_REQUEST);
            responseDto.setMessage(e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
        }

        return responseDto;
    }

    @PostMapping("/board/delete")
    public BasicResponseDto deleteBoard(@RequestBody DeleteBoardDto dto,
                                        HttpSession session,
                                        HttpServletResponse response) {

        BasicResponseDto responseDto = BasicResponseDto.builder()
                .httpStatus(OK)
                .build();

        try {
            Object attribute = session.getAttribute("member");
            if (attribute != null) {
                Member member = (Member) attribute;
                dto.setMember(member);
            }

            boardService.deleteBoard(dto);

        } catch (Exception e) {
            responseDto.setHttpStatus(BAD_REQUEST);
            responseDto.setMessage(e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
        }

        return responseDto;
    }

    @PostMapping("/board/update")
    public BoardResponseDto updateBoard(@RequestBody UpdateBoardDto dto,
                                        HttpSession session,
                                        HttpServletResponse response) {

        BoardResponseDto responseDto = BoardResponseDto.builder()
                .httpStatus(OK)
                .build();
        try {
            Object attribute = session.getAttribute("member");
            if (attribute != null) {
                Member member = (Member) attribute;
                dto.setMember(member);
            }

            boardService.updateBoard(dto);

        } catch (Exception e) {
            responseDto.setHttpStatus(BAD_REQUEST);
            responseDto.setMessage(e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
        }

        return responseDto;
    }

    @GetMapping("/board/{id}")
    public BoardQueryResponseDto getBoard(@PathVariable("id") Long id,
                                          HttpServletResponse response) {

        BoardQueryResponseDto responseDto = BoardQueryResponseDto.builder()
                .httpStatus(OK)
                .build();

        try {
            BoardNoPw board = boardService.getBoard(id);
            responseDto.setBoardNoPw(board);

        } catch (NullPointerException e) {
            responseDto.setHttpStatus(NOT_FOUND);
            responseDto.setMessage(e.getMessage());
            response.setStatus(SC_NOT_FOUND);
        }

        return responseDto;
    }

}
