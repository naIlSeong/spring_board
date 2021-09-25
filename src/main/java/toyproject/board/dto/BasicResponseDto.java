package toyproject.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

// 기본 응답 DTO
@Getter
@Setter
@SuperBuilder
public class BasicResponseDto {

    private HttpStatus httpStatus;
    private String message;

}
