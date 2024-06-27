package kr.iam.domain.category.dao;

import kr.iam.domain.category.domain.Category;
import kr.iam.domain.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c")
    List<Category> findAllCategory();

    Optional<Category> findByChannel(Channel channel);

    Optional<Category> findByName(String name);
}
