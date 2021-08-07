package toyproject.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import toyproject.board.dto.BasicResponseDto;

import java.util.List;

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
     * Validation 예외
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BasicResponseDto> validationException(MethodArgumentNotValidException e) {

        List<ObjectError> errors = e.getBindingResult().getAllErrors();

        BasicResponseDto dto = BasicResponseDto.builder()
                .httpStatus(BAD_REQUEST)
                .message(errors.get(0).getDefaultMessage())
                .build();

        return ResponseEntity
                .badRequest()
                .body(dto);
    }

}
