package toyproject.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberNoPw {

    private Long memberId;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @QueryProjection
    public MemberNoPw(Long memberId, String username, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.memberId = memberId;
        this.username = username;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

}
