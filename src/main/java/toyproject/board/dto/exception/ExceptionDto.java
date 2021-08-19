package toyproject.board.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionDto {

    private String field;
    private String message;

    @Builder
    public ExceptionDto(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
