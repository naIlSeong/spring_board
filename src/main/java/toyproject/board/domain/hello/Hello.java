package toyproject.board.domain.hello;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Hello {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Builder
    public Hello(String name) {
        this.name = name;
    }
}
