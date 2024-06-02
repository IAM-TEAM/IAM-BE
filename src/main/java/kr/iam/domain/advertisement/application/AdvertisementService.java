package kr.iam.domain.advertisement.application;

import kr.iam.domain.advertisement.dao.AdvertisementRepository;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    /**
     * 광고 찾기
     * @param advertiseId
     * @return
     */
    public Advertisement findByAdvertiseId(Long advertiseId) {
        return advertisementRepository.findById(advertiseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADVERTISE_NOT_FOUND));
    }
}
