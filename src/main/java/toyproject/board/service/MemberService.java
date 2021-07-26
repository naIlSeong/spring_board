package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.member.Member;
import toyproject.board.domain.member.MemberQueryRepository;
import toyproject.board.domain.member.MemberRepository;
import toyproject.board.dto.member.MemberDto;
import toyproject.board.dto.member.MemberNoPw;
import toyproject.board.dto.member.MemberSearchCondition;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

    @Transactional
    public Long join(MemberDto dto) throws IllegalArgumentException {

        // 입력 받은 값이 유효한지 체크
        if (!isValid(dto)) {
            throw new IllegalArgumentException("이름과 비밀번호는 필수 값입니다.");
        }

        // 문자열 다듬기
        dto.setUsername(dto.getUsername()
                .toLowerCase()
                .trim()
                .replaceAll(" ", "_"));

        // 동일한 username 존재하는지 체크
        Optional<Member> existUser = memberRepository.findByUsername(dto.getUsername());
        if (existUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }

        // username 길이 체크
        if (dto.getUsername().length() < 4 || dto.getUsername().length() > 24) {
            throw new IllegalArgumentException("이름의 길이는 4자 이상, 24자 이하입니다.");
        }

        // password 길이 체크
        if (dto.getPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호의 길이는 8자 이상입니다.");
        }

        // 비밀번호 암호화
        dto.setPassword(
                BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10)));

        Member member = dto.toEntity();
        memberRepository.save(member);

        return member.getId();
    }

    private boolean isValid(MemberDto dto) {
        return hasText(dto.getUsername()) && hasText(dto.getPassword());
    }

    @Transactional(readOnly = true)
    public Member login(MemberDto dto) {

        // 입력 받은 값이 유효한지 체크
        if (!isValid(dto)) {
            throw new IllegalArgumentException("이름과 비밀번호는 필수 값입니다.");
        }

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

        memberRepository.delete(member);

        return true;
    }

    @Transactional(readOnly = true)
    public MemberNoPw getMember(Long memberId) {

        MemberNoPw member = memberQueryRepository.findById(memberId);
        if (member == null) {
            throw new NullPointerException("유저를 찾을 수 없습니다.");
        }

        return member;
    }

    @Transactional(readOnly = true)
    public List<MemberNoPw> searchMember(MemberSearchCondition condition) {
        return memberQueryRepository.searchMember(condition);
    }

}
