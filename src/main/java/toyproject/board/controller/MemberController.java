package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.MemberDto;
import toyproject.board.dto.MemberNoPw;
import toyproject.board.dto.MemberResponseDto;
import toyproject.board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/new")
    public BasicResponseDto join(@RequestBody MemberDto dto,
                                 HttpServletResponse response) {

        BasicResponseDto responseDto = BasicResponseDto.builder()
                .httpStatus(OK)
                .build();
        response.setStatus(SC_CREATED);

        try {

            memberService.join(dto);

        } catch (IllegalArgumentException e) {
            responseDto.setHttpStatus(BAD_REQUEST);
            responseDto.setMessage(e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
        }

        return responseDto;
    }

    @PostMapping("/member/login")
    public BasicResponseDto login(@RequestBody MemberDto dto,
                                  HttpSession session,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        BasicResponseDto responseDto = BasicResponseDto.builder()
                .httpStatus(OK)
                .build();

        try {
            Member member = memberService.login(dto);
            session.setAttribute("member", member);

            String userAgent = request.getHeader("User-Agent");
            session.setAttribute("userAgent", userAgent);

            String ip = request.getHeader("X-FORWARDED-FOR");
            if (ip == null) {
                ip = request.getRemoteAddr();
            }
            session.setAttribute("ip", ip);

        } catch (IllegalArgumentException e) {
            responseDto.setHttpStatus(BAD_REQUEST);
            responseDto.setMessage(e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
        }

        return responseDto;
    }

    @PostMapping("/member/withdrawal")
    public BasicResponseDto withdrawal(HttpSession session,
                                       HttpServletResponse response) {

        BasicResponseDto responseDto = BasicResponseDto.builder()
                .httpStatus(OK)
                .build();

        try {
            Object attribute = session.getAttribute("member");
            Member member = (Member) attribute;

            memberService.withdrawal(member.getId());
            session.invalidate();

        } catch (NullPointerException e) {
            responseDto.setHttpStatus(BAD_REQUEST);
            responseDto.setMessage(e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
        } catch (Exception e) {
            responseDto.setHttpStatus(INTERNAL_SERVER_ERROR);
            responseDto.setMessage("알 수 없는 문제가 발생했습니다.");
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }

        return responseDto;
    }

    @GetMapping("/member/{memberId}")
    public BasicResponseDto getMember(
            @PathVariable(name = "memberId") Long memberId,
            HttpServletResponse response) {

        try {
            MemberNoPw member = memberService.getMember(memberId);

            return MemberResponseDto.builder()
                    .httpStatus(OK)
                    .member(member)
                    .build();

        } catch (NullPointerException e) {
            BasicResponseDto responseDto = BasicResponseDto.builder()
                    .httpStatus(NOT_FOUND)
                    .message(e.getMessage())
                    .build();

            response.setStatus(SC_NOT_FOUND);

            return responseDto;
        }

    }

}
