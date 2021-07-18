package toyproject.board.domain.hello;

import java.util.List;

public interface HelloRepositoryCustom {

    List<Hello> findAllByUsername(String username);

}
