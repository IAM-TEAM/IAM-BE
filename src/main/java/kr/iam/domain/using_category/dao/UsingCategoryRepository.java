package kr.iam.domain.using_category.dao;

import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.using_category.domain.UsingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsingCategoryRepository extends JpaRepository<UsingCategory, Long> {

    void deleteAllByChannel(Channel channel);
}
