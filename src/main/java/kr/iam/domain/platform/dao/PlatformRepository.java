package kr.iam.domain.platform.dao;

import kr.iam.domain.platform.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
