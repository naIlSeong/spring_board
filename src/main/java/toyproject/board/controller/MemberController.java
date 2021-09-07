package toyproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.board.query.BoardAndCommentCount;
import toyproject.board.dto.board.query.BoardSearchCondition;
import toyproject.board.dto.member.command.MemberRequestDto;
import toyproject.board.dto.member.query.MemberQueryDto;
import toyproject.board.dto.member.query.MemberSearchCondition;
import toyproject.board.dto.member.response.MemberDetailResponseDto;
import toyproject.board.service.BoardService;
import toyproject.board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;

    @ResponseStatus(CREATED)
    @PostMapping("/new")
    public BasicResponseDto join(@RequestBody MemberRequestDto dto) {

        memberService.join(dto);

        return BasicResponseDto.builder()
                .httpStatus(CREATED)
                .build();
    }

    @PostMapping("/login")
    public BasicResponseDto login(@RequestBody MemberRequestDto dto,
                                  HttpSession session,
                                  HttpServletRequest request) {

        Member member = memberService.login(dto);
        session.setAttribute("member", member);

        String userAgent = request.getHeader("User-Agent");
        session.setAttribute("userAgent", userAgent);

        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        session.setAttribute("ip", ip);

        return BasicResponseDto.builder()
                .httpStatus(OK)
                .build();
    }

    @PostMapping("/withdrawal")
    public BasicResponseDto withdrawal(@SessionAttribute("member") Member member,
                                       SessionStatus sessionStatus) {

        memberService.withdrawal(member.getId());
        sessionStatus.setComplete();

        return BasicResponseDto.builder()
                .httpStatus(OK)
                .build();
    }

    @GetMapping("/{id}")
    public MemberDetailResponseDto getMemberDetail(@PathVariable(name = "id") Long memberId,
                                                   Pageable pageable) {

        MemberQueryDto member = memberService.getMember(memberId);

        BoardSearchCondition condition = BoardSearchCondition.builder()
                .memberId(memberId)
                .build();
        Page<BoardAndCommentCount> boardList = boardService.searchBoard(condition, pageable);

        return MemberDetailResponseDto.builder()
                .httpStatus(OK)
                .member(member)
                .boardList(boardList)
                .build();
    }

    @GetMapping("/search")
    public Page<MemberQueryDto> searchMember(MemberSearchCondition condition, Pageable pageable) {
        return memberService.searchMember(condition, pageable);
    }

}
