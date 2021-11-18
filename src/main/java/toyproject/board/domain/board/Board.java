package toyproject.board.domain.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.board.domain.BaseEntity;
import toyproject.board.domain.member.Member;
import toyproject.board.dto.board.query.BoardQueryDto;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private Integer views;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 비로그인 일 때
     */
    private String nickname;
    private String password;

    @Builder
    public Board(String title, String content, Member member, String nickname, String password) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.nickname = nickname;
        this.password = password;
        this.views = 0;
    }

    //=====수정 메서드=====//
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateViews() {
        this.views += 1;
    }

    //=====DTO 생성 메서드=====//
    public BoardQueryDto toQueryDto() {

        BoardQueryDto.BoardQueryDtoBuilder builder = BoardQueryDto.builder()
                .boardId(id)
                .title(title)
                .content(content)
                .nickname(nickname)
                .views(views)
                .createdDate(this.getCreatedDate())
                .lastModifiedDate(this.getLastModifiedDate());

        if (this.member != null) {
            builder.memberId(member.getId());
        }

        return builder.build();
    }

}
