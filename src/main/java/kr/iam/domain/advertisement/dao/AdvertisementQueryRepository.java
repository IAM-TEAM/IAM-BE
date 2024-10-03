package kr.iam.domain.advertisement.dao;

import kr.iam.domain.advertisement.dto.info.AdvertisementInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AdvertisementQueryRepository {
    Page<AdvertisementInfo> advertisementList(Pageable pageable);
}
