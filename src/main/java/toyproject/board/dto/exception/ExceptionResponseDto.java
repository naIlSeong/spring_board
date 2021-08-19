package toyproject.board.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExceptionResponseDto {

    private HttpStatus httpStatus;
    private List<ExceptionDto> exceptions = new ArrayList<>();

    @Builder
    public ExceptionResponseDto(HttpStatus httpStatus, List<ExceptionDto> exceptions) {
        this.httpStatus = httpStatus;
        this.exceptions = exceptions;
    }

    public void addException(ExceptionDto ex) {
        this.exceptions.add(ex);
    }

}