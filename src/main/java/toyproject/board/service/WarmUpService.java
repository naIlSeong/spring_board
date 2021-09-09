package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Profile("test")
@Component
public class WarmUpService {

    private final WarmUpDBService warmUpDBService;

    @PostConstruct
    public void warmUp() {
        warmUpDBService.warmUp();
    }

    @RequiredArgsConstructor
    @Component
    @Transactional(readOnly = true)
    @Slf4j
    static class WarmUpDBService {

        private final EntityManager em;

        public void warmUp() {
            warmUpBoard();
            warmUpMember();
            warmUpComment();
        }

        private void warmUpBoard() {
            log.info("Warm up DB - table : Board");
            em.createQuery("select b from Board b")
                    .getResultList();
        }

        private void warmUpMember() {
            log.info("Warm up DB - table : Member");
            em.createQuery("select m from Member m")
                    .getResultList();
        }

        private void warmUpComment() {
            log.info("Warm up DB - table : Comment");
            em.createQuery("select c from Comment c")
                    .getResultList();
        }

    }

}
