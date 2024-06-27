package kr.iam.domain.detail_category.dao;

import kr.iam.domain.detail_category.domain.DetailCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetailCategoryRepository extends JpaRepository<DetailCategory, Long> {

}
