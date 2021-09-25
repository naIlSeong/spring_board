package toyproject.board.dto.member.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 멤버 조회 DTO (비밀번호 X)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberQueryDto {

    private Long memberId;
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @QueryProjection
    public MemberQueryDto(Long memberId, String username, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.memberId = memberId;
        this.username = username;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

}
