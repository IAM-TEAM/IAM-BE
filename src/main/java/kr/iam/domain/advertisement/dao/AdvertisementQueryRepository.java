package kr.iam.domain.advertisement.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static kr.iam.domain.advertisement.dto.AdvertisementDto.AdvertisementInfo;

public interface AdvertisementQueryRepository {
    Page<AdvertisementInfo> advertisementList(Pageable pageable);
}
