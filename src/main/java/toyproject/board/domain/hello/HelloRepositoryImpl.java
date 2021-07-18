package toyproject.board.domain.hello;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static toyproject.board.domain.hello.QHello.hello;

@RequiredArgsConstructor
public class HelloRepositoryImpl implements HelloRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Hello> findAllByUsername(String username) {
        return queryFactory
                .selectFrom(hello)
                .where(hello.name.eq(username))
                .fetch();
    }
}
