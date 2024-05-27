package kr.iam.domain.advertisement.application;

import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.advertisement.dao.AdvertisementRepository;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.advertisement.dto.AdvertisementDto;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static kr.iam.domain.advertisement.dto.AdvertisementDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final S3UploadUtil s3UploadUtil;
    private final CookieUtil cookieUtil;

    /**
     * 광고 찾기
     * @param advertiseId
     * @return
     */
    public Advertisement findByAdvertiseId(Long advertiseId) {
        return advertisementRepository.findById(advertiseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADVERTISE_NOT_FOUND));
    }

    @Transactional
    public Long enrollAdvertisement(MultipartFile file, EnrollAdvertisementDto enrollAdvertisementDto,
                                    HttpServletRequest request) throws IOException {
        Long memberId = Long.valueOf(cookieUtil.getCookieValue("memberId", request));
        String s = null;
        if(!file.isEmpty()) {
            s = s3UploadUtil.saveFile(file, LocalDateTime.now(), memberId);
        }
        Advertisement advertisement = Advertisement.toAdvertisement(enrollAdvertisementDto, s);
        return advertisement.getId();
    }
}
