package toyproject.board.domain.member.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toyproject.board.dto.member.query.MemberQueryDto;
import toyproject.board.dto.member.query.MemberSearchCondition;

public interface MemberQueryRepositoryCustom {

    MemberQueryDto findNoPasswordById(Long memberId);

    Page<MemberQueryDto> searchPage(MemberSearchCondition condition, Pageable pageable);

}
