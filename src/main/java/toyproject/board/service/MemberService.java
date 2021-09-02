package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import toyproject.board.domain.board.BoardRepository;
import toyproject.board.domain.comment.CommentRepository;
import toyproject.board.domain.member.Member;
import toyproject.board.domain.member.query.MemberQueryRepository;
import toyproject.board.domain.member.MemberRepository;
import toyproject.board.dto.member.command.MemberRequestDto;
import toyproject.board.dto.member.query.MemberQueryDto;
import toyproject.board.dto.member.query.MemberSearchCondition;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Validated
    public Long join(@Valid MemberRequestDto dto) {

        // 문자열 다듬기
        dto.setUsername(dto.getUsername()
                .toLowerCase()
                .trim()
                .replaceAll(" ", "_"));

        // 동일한 username 존재하는지 체크
        Optional<Member> existMember = memberRepository.findByUsername(dto.getUsername());
        if (existMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }

        // 비밀번호 암호화
        dto.setPassword(
                BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10)));

        Member member = dto.toEntity();
        memberRepository.save(member);

        return member.getId();
    }

    @Validated
    public Member login(@Valid MemberRequestDto dto) {

        Member member = memberRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("이름을 다시 확인해주세요."));

        boolean isMatch = BCrypt.checkpw(dto.getPassword(), member.getPassword());
        if (!isMatch) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해주세요.");
        }

        return member;
    }

    @Transactional
    public boolean withdrawal(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NullPointerException("유저를 찾을 수 없습니다."));

        commentRepository.deleteByMemberId(memberId); // 유저가 작성한 댓글
        boardRepository.deleteByMemberId(memberId); // 유저가 작성한 게시물 ( +게시물에 포함된 댓글 )
        memberRepository.delete(member); // 유저 삭제

        return true;
    }

    public MemberQueryDto getMember(Long memberId) {

        MemberQueryDto member = memberQueryRepository.findNoPasswordById(memberId);
        if (member == null) {
            throw new NullPointerException("유저를 찾을 수 없습니다.");
        }

        return member;
    }

    public Page<MemberQueryDto> searchMember(MemberSearchCondition condition, Pageable pageable) {
        return memberQueryRepository.searchPage(condition, pageable);
    }

}
