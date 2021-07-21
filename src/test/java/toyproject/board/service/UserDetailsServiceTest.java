package toyproject.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.dto.MemberDto;

@SpringBootTest
@Transactional
@Rollback(value = false)
class UserDetailsServiceTest {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    MemberService memberService;

    @Test
    void 로그인() throws Exception {
        // give
        MemberDto dto = MemberDto.builder()
                .username("test")
                .password("12341234")
                .build();

        Long memberId = memberService.join(dto);

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());

        // then
        System.out.println("userDetails = " + userDetails);
        System.out.println("userDetails.getAuthorities() = " + userDetails.getAuthorities());

    }

}