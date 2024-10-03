package kr.iam.domain.advertisement.application;

import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.advertisement.dto.info.AdvertisementInfo;
import kr.iam.domain.advertisement.dto.req.AdvertisementSaveReqDto;
import kr.iam.domain.advertisement.dto.res.AdvertisementResDto;
import kr.iam.domain.advertisement.helper.AdvertisementHelper;
import kr.iam.domain.advertisement.mapper.AdvertisementMapper;
import kr.iam.global.aspect.member.MemberInfoParam;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementService {

    private final AdvertisementHelper advertisementHelper;
    private final AdvertisementMapper advertisementMapper;
    private final S3UploadUtil s3UploadUtil;
    private final CookieUtil cookieUtil;

    @Transactional
    public Long enrollAdvertisement(MultipartFile file, AdvertisementSaveReqDto advertisementSaveReqDto,
                                    MemberInfoParam memberInfoParam) throws IOException {
        Long memberId = memberInfoParam.memberId();
        String s = null;
        if(!file.isEmpty()) {
            s = s3UploadUtil.saveFile(file, LocalDateTime.now(), memberId);
        }
        Advertisement advertisement = Advertisement.toAdvertisement(advertisementSaveReqDto, s);
        advertisementHelper.save(advertisement);
        return advertisement.getId();
    }

    public AdvertisementResDto getAdvertisements(Pageable pageable) {
        Page<AdvertisementInfo> advertisementInfos = advertisementHelper.findAdvertisements(pageable);
        return advertisementMapper.toAdvertisementResDto(advertisementInfos);
    }
}
