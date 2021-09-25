package toyproject.board.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 기본 예외 DTO
// ExceptionResponseDto(응답 DTO)에서 사용
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
