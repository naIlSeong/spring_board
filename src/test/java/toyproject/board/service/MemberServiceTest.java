package toyproject.board.service;

import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.member.Member;
import toyproject.board.domain.member.MemberRepository;
import toyproject.board.dto.MemberDto;
import toyproject.board.dto.MemberNoPw;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    /**
     * 회원가입 테스트
     * memberService.join();
     */
    @Tag("join")
    @Test
    void 회원가입_성공() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_값이_빠짐_1() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_값이_빠짐_2() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("test")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름과 비밀번호는 필수 값입니다.");
    }

    @Tag("join")
    @Test
    void 회원가입_값이_빠짐_3() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .password("1234")
                .build();

        // when
        // memberService.join(dto);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(dto))
                .withMessage("이름과 비밀번호는 필수 값입니다.");
    }

    @Tag("join")
    @Test
    void 회원가입_값이_빠짐_4() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_같은_이름의_회원이_존재_1() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();

        MemberDto sameUsernameDto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_같은_이름의_회원이_존재_2() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();

        MemberDto sameUsernameDto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_같은_이름의_회원이_존재_3() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("te st")
                .password("12341234")
                .build();

        MemberDto sameUsernameDto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_이름_길이_1() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_이름_길이_2() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_이름_길이_3() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_비밀번호_길이() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
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

    @Tag("join")
    @Test
    void 회원가입_암호화() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
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

    /**
     * 로그인 테스트
     * memberService.login()
     */
    @Tag("login")
    @Test
    void 로그인_성공() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();
        memberService.join(dto);

        // when
        MemberDto loginDto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();


        memberService.login(loginDto);

        // then
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println("authentication = " + authentication);

    }

    @Tag("login")
    @Test
    void 로그인_실패_이름() throws Exception {
        // give

        // when
        MemberDto loginDto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();

        // memberService.login(loginDto);

        // then
        assertThatThrownBy(() -> memberService.login(loginDto))
                .hasMessage("이름을 다시 확인해주세요.");

    }

    @Tag("login")
    @Test
    void 로그인_실패_비밀번호() throws Exception {
        // give
        MemberDto memberDto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();

        memberService.join(memberDto);

        // when
        MemberDto loginDto = MemberDto.builder()
                .username("test")
                .password("43214321")
                .build();

        // memberService.login(loginDto);

        // then
        assertThatThrownBy(() -> memberService.login(loginDto))
                .hasMessage("비밀번호를 다시 확인해주세요.");

    }

    /**
     * 회원 탈퇴 테스트
     * memberService.withdrawal()
     */
    @Tag("withdrawal")
    @Test
    void 탈퇴_성공() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();
        Long memberId = memberService.join(dto);

        em.flush();
        em.clear();

        // when
        boolean result = memberService.withdrawal(memberId);

        // then
        assertThat(result).isTrue();
    }

    @Tag("withdrawal")
    @Test
    void 탈퇴_예외() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();
        memberService.join(dto);

        em.flush();
        em.clear();

        // when
        // memberService.withdrawal(12121L);

        // then
        assertThatThrownBy(() -> memberService.withdrawal(12121L))
                .hasMessage("유저를 찾을 수 없습니다.");
    }

    /**
     * 회원 상세 조회 테스트
     * memberService.getMember()
     */
    @Tag("getMember")
    @Test
    void 상세_조회_성공() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();

        Long memberId = memberService.join(dto);

        // when
        MemberNoPw result = memberService.getMember(memberId);

        // then
        assertThat(result.getUsername()).isEqualTo("test");
        assertThat(result.getMemberId()).isEqualTo(memberId);

    }

    @Tag("getMember")
    @Test
    void 상세_조회_성공2() throws Exception {
        // give
        MemberDto dto1 = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();
        MemberDto dto2 = MemberDto.builder()
                .username("test2")
                .password("12341234")
                .build();

        Long id1 = memberService.join(dto1);
        Long id2 = memberService.join(dto2);

        // when
        MemberNoPw result1 = memberService.getMember(id1);
        MemberNoPw result2 = memberService.getMember(id2);

        // then
        assertThat(result1.getUsername()).isEqualTo("test");
        assertThat(result2.getUsername()).isEqualTo("test2");


    }

    @Tag("getMember")
    @Test
    void 상세_조회_예외() throws Exception {
        // give

        // when
        // memberService.getMember(12L);

        // then
        assertThatThrownBy(() -> memberService.getMember(12L))
                .hasMessage("유저를 찾을 수 없습니다.");

    }
}