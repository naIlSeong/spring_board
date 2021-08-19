package toyproject.board.dto.board.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckPasswordDto {

    private Long id;
    private String password;

    @QueryProjection
    public CheckPasswordDto(Long id, String password) {
        this.id = id;
        this.password = password;
    }
}
