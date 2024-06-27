package kr.iam.domain.detail_category.dao;

import kr.iam.domain.detail_category.domain.DetailCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DetailCategoryRepository extends JpaRepository<DetailCategory, Long> {

    @Query("select d from DetailCategory d join fetch d.category c where d.name = :subName")
    Optional<DetailCategory> findByName(String subName);
}
