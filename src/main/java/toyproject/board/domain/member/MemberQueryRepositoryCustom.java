package toyproject.board.domain.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toyproject.board.dto.member.MemberNoPw;
import toyproject.board.dto.member.MemberSearchCondition;

import java.util.List;

public interface MemberQueryRepositoryCustom {

    MemberNoPw findNoPasswordById(Long memberId);

    List<MemberNoPw> searchMember(MemberSearchCondition condition);

    Page<MemberNoPw> searchPage(MemberSearchCondition condition, Pageable pageable);

}
