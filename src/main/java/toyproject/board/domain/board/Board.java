package toyproject.board.domain.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.board.domain.BaseEntity;
import toyproject.board.domain.member.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
