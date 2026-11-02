package spring.splearn.application.member.provided;

import jakarta.validation.Valid;
import spring.splearn.domain.member.Member;
import spring.splearn.domain.member.MemberInfoUpdateRequest;
import spring.splearn.domain.member.MemberRegisterRequest;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long memberId);

    Member deactivate(Long memberId);

    Member updateInfo(Long memberId, @Valid MemberInfoUpdateRequest memberInfoUpdateRequest);
}
