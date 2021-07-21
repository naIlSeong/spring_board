package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.MemberDto;
import toyproject.board.service.MemberService;

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

}
