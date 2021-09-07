package toyproject.board.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import toyproject.board.dto.BasicResponseDto;
import toyproject.board.dto.exception.ExceptionDto;
import toyproject.board.dto.exception.ExceptionResponseDto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionController {

    /**
     * 상태 코드 : 404
     */
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<BasicResponseDto> notFoundException(NullPointerException e) {

        BasicResponseDto dto = BasicResponseDto.builder()
                .httpStatus(NOT_FOUND)
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(NOT_FOUND)
                .body(dto);
    }

    /**
     * 상태 코드 : 400
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<BasicResponseDto> badRequestException(IllegalArgumentException e) {

        BasicResponseDto dto = BasicResponseDto.builder()
                .httpStatus(BAD_REQUEST)
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .badRequest()
                .body(dto);
    }

    /**
     * 상태 코드 : 400
     * Validation 예외 - 컨트롤러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> validationException(MethodArgumentNotValidException e) {

        ExceptionResponseDto responseDto = ExceptionResponseDto.builder()
                .httpStatus(BAD_REQUEST)
                .exceptions(new ArrayList<>())
                .build();

        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        for (FieldError error : errors) {

            ExceptionDto exceptionDto = ExceptionDto.builder()
                    .field(error.getField())
                    .message(error.getDefaultMessage())
                    .build();

            responseDto.addException(exceptionDto);

        }

        return ResponseEntity
                .badRequest()
                .body(responseDto);
    }

    /**
     * 상태 코드 : 400
     * Validation 예외 - 서비스
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDto> validationException(ConstraintViolationException e) {

        ExceptionResponseDto responseDto = ExceptionResponseDto.builder()
                .httpStatus(BAD_REQUEST)
                .exceptions(new ArrayList<>())
                .build();

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {

            Iterator<Path.Node> iterator = violation.getPropertyPath().iterator();
            String field = null;
            while (iterator.hasNext()) {
                field = iterator.next().getName();
            }

            ExceptionDto exceptionDto = ExceptionDto.builder()
                    .field(field)
                    .message(violation.getMessage())
                    .build();

            responseDto.addException(exceptionDto);

        }

        return ResponseEntity
                .badRequest()
                .body(responseDto);
    }
}
