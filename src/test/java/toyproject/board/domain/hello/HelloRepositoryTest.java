package toyproject.board.domain.hello;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class HelloRepositoryTest {

    @Autowired
    HelloRepository helloRepository;

    @Test
    void helloTest() throws Exception {
        // give
        Hello testHello = Hello.builder()
                .name("test")
                .build();
        helloRepository.save(testHello);

        // when
        Optional<Hello> findHello = helloRepository.findById(testHello.getId());

        // then
        assertThat(findHello.get()).isEqualTo(testHello);

    }

    @Test
    void helloTest2() throws Exception {
        // give
        Hello testHello = Hello.builder()
                .name("test")
                .build();
        Hello testHello2 = Hello.builder()
                .name("test")
                .build();

        helloRepository.save(testHello);
        helloRepository.save(testHello2);

        // when
        List<Hello> result = helloRepository.findAllByUsername("test");

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("name")
                .containsExactly("test", "test");

    }

}