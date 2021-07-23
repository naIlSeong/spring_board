package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.MemberDto;
import toyproject.board.service.MemberService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/new")
    public Long join(@RequestBody MemberDto dto) {
        return memberService.join(dto);
    }

    @PostMapping("/member/login")
    public Member login(@RequestBody MemberDto dto, HttpSession session) {
        Member member = memberService.login(dto);

        session.setAttribute("member", member);
        return member;
    }

    @PostMapping("/member/withdrawal")
    public BasicResponseDto withdrawal(HttpSession session, HttpServletResponse response) {
        Object attribute = session.getAttribute("member");
        Member member = (Member) attribute;

        boolean result = memberService.withdrawal(member.getId());

        BasicResponseDto dto = BasicResponseDto.builder()
                .httpStatus(HttpStatus.OK)
                .build();

        if (!result) {
            dto.setHttpStatus(HttpStatus.BAD_REQUEST);
            dto.setMessage("유저를 찾을 수 없습니다.");
            response.setStatus(400);
        }

        return dto;
    }

}
