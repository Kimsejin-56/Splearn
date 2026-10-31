package spring.splearn.application.provided;

import spring.splearn.domain.Member;

/**
 * 회원의 정보 조회와 관련된 기능을 제공한다.
 */

public interface MemberFinder {
    Member find(Long memberId);
}
