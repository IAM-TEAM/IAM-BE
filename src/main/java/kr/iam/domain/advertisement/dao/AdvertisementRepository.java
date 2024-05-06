package kr.iam.domain.advertisement.dao;

import kr.iam.domain.advertisement.domain.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

}
