package kr.iam.domain.member.dao;

import kr.iam.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByUsername(String username);

    Member findByUsername(String username);

    boolean existsById(Long memberId);
}
