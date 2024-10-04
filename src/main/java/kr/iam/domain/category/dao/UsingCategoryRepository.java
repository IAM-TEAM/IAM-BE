package kr.iam.domain.category.dao;

import kr.iam.domain.category.domain.UsingCategory;
import kr.iam.domain.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsingCategoryRepository extends JpaRepository<UsingCategory, Long> {

    void deleteAllByChannel(Channel channel);
}
