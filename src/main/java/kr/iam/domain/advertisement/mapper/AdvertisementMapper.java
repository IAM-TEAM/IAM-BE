package kr.iam.domain.advertisement.mapper;

import kr.iam.domain.advertisement.dto.info.AdvertisementInfo;
import kr.iam.domain.advertisement.dto.res.AdvertisementResDto;
import kr.iam.global.domain.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementMapper {
    public AdvertisementResDto toAdvertisementResDto(Page<AdvertisementInfo> advertisementInfoPage) {
        PageInfo pageInfo = PageInfo.of(advertisementInfoPage);
        return AdvertisementResDto.of(advertisementInfoPage.getContent(), pageInfo);
    }
}
