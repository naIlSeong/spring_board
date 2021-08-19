package toyproject.board.dto.board.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BoardSearchCondition {

    private String nickname;
    private String title;
    private String content;
    private Boolean isAsc;

    @Builder
    public BoardSearchCondition(String nickname, String title, String content, Boolean isAsc) {
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.isAsc = isAsc;
    }
}
