package toyproject.board.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

//@NoArgsConstructor
@ToString
@Getter
@Setter
public class BasicResponseDto {

    private HttpStatus httpStatus;
    private String message;

    @Builder
    public BasicResponseDto(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
