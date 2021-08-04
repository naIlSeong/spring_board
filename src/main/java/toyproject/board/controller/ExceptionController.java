package toyproject.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import toyproject.board.dto.BasicResponseDto;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<BasicResponseDto> notFoundException(final NullPointerException e) {

        BasicResponseDto dto = BasicResponseDto.builder()
                .httpStatus(NOT_FOUND)
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(NOT_FOUND)
                .body(dto);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<BasicResponseDto> badRequestException(final IllegalArgumentException e) {

        BasicResponseDto dto = BasicResponseDto.builder()
                .httpStatus(BAD_REQUEST)
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .badRequest()
                .body(dto);
    }

}
