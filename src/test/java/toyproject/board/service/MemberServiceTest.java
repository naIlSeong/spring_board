package toyproject.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.member.Member;
import toyproject.board.domain.member.MemberRepository;
import toyproject.board.dto.JoinRequestDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    /**
     * 회원가입 테스트
     * memberService.join();
     */
    @Test
    void 회원가입_성공() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("test")
                .password("12341234")
                .build();

        // when
        Long memberId = memberService.join(dto);

        // then
        Optional<Member> joinedMember = memberRepository.findById(memberId);
        Member result = joinedMember.get();

        assertThat(result.getUsername()).isEqualTo("test");
    }

    @Test
    void 회원가입_값이_빠짐_1() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("test")
                .password("  ")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름과 비밀번호는 필수 값입니다.");
    }

    @Test
    void 회원가입_값이_빠짐_2() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("test")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름과 비밀번호는 필수 값입니다.");
    }

    @Test
    void 회원가입_값이_빠짐_3() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .password("1234")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름과 비밀번호는 필수 값입니다.");
    }

    @Test
    void 회원가입_값이_빠짐_4() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("")
                .password("1234")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름과 비밀번호는 필수 값입니다.");
    }

    @Test
    void 회원가입_같은_이름의_회원이_존재_1() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("test")
                .password("12341234")
                .build();

        JoinRequestDto sameUsernameDto = JoinRequestDto.builder()
                .username("TEST")
                .password("43214321")
                .build();

        // when
        memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(sameUsernameDto))
                .withMessage("이미 존재하는 이름입니다.");
    }

    @Test
    void 회원가입_같은_이름의_회원이_존재_2() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("test")
                .password("12341234")
                .build();

        JoinRequestDto sameUsernameDto = JoinRequestDto.builder()
                .username("  TEST  ")
                .password("43214321")
                .build();

        // when
        memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(sameUsernameDto))
                .withMessage("이미 존재하는 이름입니다.");
    }

    @Test
    void 회원가입_같은_이름의_회원이_존재_3() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("te st")
                .password("12341234")
                .build();

        JoinRequestDto sameUsernameDto = JoinRequestDto.builder()
                .username("  TE_ST  ")
                .password("43214321")
                .build();

        // when
        memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(sameUsernameDto))
                .withMessage("이미 존재하는 이름입니다.");
    }

    @Test
    void 회원가입_이름_길이_1() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("testtesttesttesttesttest1")
                .password("12341234")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름의 길이는 4자 이상, 24자 이하입니다.");
    }

    @Test
    void 회원가입_이름_길이_2() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("가나다라가나다라가나다라가나다라가나다라가나다라1")
                .password("12341234")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름의 길이는 4자 이상, 24자 이하입니다.");
    }

    @Test
    void 회원가입_이름_길이_3() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("  가나다  ")
                .password("12341234")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름의 길이는 4자 이상, 24자 이하입니다.");
    }

    @Test
    void 회원가입_비밀번호_길이() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("test")
                .password("1234567")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("비밀번호의 길이는 8자 이상입니다.");
    }

    @Test
    void 회원가입_암호화() throws Exception {
        // give
        JoinRequestDto dto = JoinRequestDto.builder()
                .username("test")
                .password("12345678")
                .build();

        // when
        Long memberId = memberService.join(dto);

        // then
        Optional<Member> findMember = memberRepository.findById(memberId);
        Member result = findMember.get();

        assertThat(result.getPassword()).isNotEqualTo("12345678");
    }

}