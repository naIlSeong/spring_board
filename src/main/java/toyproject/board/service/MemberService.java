package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.member.Member;
import toyproject.board.domain.member.MemberRepository;
import toyproject.board.dto.JoinRequestDto;

import java.util.Locale;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    @Transactional
    public Long join(JoinRequestDto dto) throws IllegalArgumentException {

        // 입력 받은 값이 유효한지 체크
        if (!isValid(dto)) {
            throw new IllegalArgumentException("이름과 비밀번호는 필수 값입니다.");
        }

        dto.setUsername(dto.getUsername()
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll(" ", "_"));
        Optional<Member> existUser = memberRepository.findByUsername(dto.getUsername());

        // 동일한 username 존재하는지 체크
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

        dto.setPassword(encoder.encode(dto.getPassword()));

        Member member = dto.toEntity();
        memberRepository.save(member);

        return member.getId();
    }

    private boolean isValid(JoinRequestDto dto) {
        return hasText(dto.getUsername()) && hasText(dto.getPassword());
    }

}
