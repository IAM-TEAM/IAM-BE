package kr.iam.domain.advertisement.dto.res;

import kr.iam.domain.advertisement.dto.info.AdvertisementInfo;
import kr.iam.global.domain.PageInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record AdvertisementResDto(
        List<AdvertisementInfo> advertisementInfos,
        PageInfo pageInfo
) {
    public static AdvertisementResDto of(List<AdvertisementInfo> advertisementInfos, PageInfo pageInfo) {
        return AdvertisementResDto
                .builder()
                .advertisementInfos(advertisementInfos)
                .pageInfo(pageInfo)
                .build();
    }
}