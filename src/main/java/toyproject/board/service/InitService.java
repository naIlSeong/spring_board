package toyproject.board.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import toyproject.board.domain.board.Board;
import toyproject.board.domain.comment.Comment;
import toyproject.board.domain.member.Member;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 더미 데이터를 집어넣기 위한 클래스
 */

@Profile("local")
@RequiredArgsConstructor
@Component
public class InitService {

    private final InitDataService initDataService;

    @PostConstruct
    public void init() {
        initDataService.init();
    }

    @RequiredArgsConstructor
    @Component
    @Transactional
    static class InitDataService {

        private final EntityManager em;

        public void init() {

            // 멤버 30명 초기화
            for (int i = 0; i < 30; i++) {
                Member member = Member.builder()
                        .username("user" + (i + 1))
                        .password(BCrypt.hashpw("12341234", BCrypt.gensalt(10)))
                        .build();
                em.persist(member);
            }

            // 게시물 111개 초기화
            for (int i = 0; i < 111; i++) {

                Member member1 = em.find(Member.class, 1L);
                Member member2 = em.find(Member.class, 2L);

                if (i % 5 == 0) {
                    Board board = Board.builder()
                            .title("title" + (i + 1))
                            .content("content" + (i + 1))
                            .nickname("user1")
                            .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                            .build();
                    em.persist(board);
                }

                if (i % 5 == 1) {
                    Board board = Board.builder()
                            .title("title" + (i + 1))
                            .content("content" + (i + 1))
                            .nickname("user2")
                            .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                            .build();
                    em.persist(board);
                }

                if (i % 5 == 2) {
                    Board board = Board.builder()
                            .title("title" + (i + 1))
                            .content("content" + (i + 1))
                            .nickname("user3")
                            .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                            .build();
                    em.persist(board);
                }

                if (i % 5 == 3) {
                    Board board = Board.builder()
                            .title("title" + (i + 1))
                            .content("content" + (i + 1))
                            .nickname(member1.getUsername())
                            .member(member1)
                            .build();
                    em.persist(board);
                }

                if (i % 5 == 4) {
                    Board board = Board.builder()
                            .title("title" + (i + 1))
                            .content("content" + (i + 1))
                            .nickname(member2.getUsername())
                            .member(member2)
                            .build();
                    em.persist(board);
                }

            }

            // 댓글 155개 초기화
            for (int i = 0; i < 31; i++) {
                for (int j = 3; j < 8; j++) {

                    Board board = em.find(Board.class, j * 11L); // 게시물 ID: 33, 44, 55, 66, 77
                    Comment comment = Comment.builder()
                            .board(board)
                            .content("comment" + (i + 1))
                            .nickname("ㅇㅇ " + (i + 1))
                            .password(BCrypt.hashpw("1234", BCrypt.gensalt(10)))
                            .build();
                    em.persist(comment);
                }
            }

        }

    }

}
