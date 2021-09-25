package toyproject.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

// 로그인, 비로그인 판단 쿼리 DTO
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
