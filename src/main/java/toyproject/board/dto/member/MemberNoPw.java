package toyproject.board.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberNoPw {

    private Long memberId;
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @QueryProjection
    public MemberNoPw(Long memberId, String username, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.memberId = memberId;
        this.username = username;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

}