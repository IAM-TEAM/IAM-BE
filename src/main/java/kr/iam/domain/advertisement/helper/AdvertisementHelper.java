package kr.iam.domain.advertisement.helper;

import kr.iam.domain.advertisement.dao.AdvertisementRepository;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.advertisement.dto.info.AdvertisementInfo;
import kr.iam.domain.advertisement.error.AdvertisementError;
import kr.iam.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvertisementHelper {
    private final AdvertisementRepository advertisementRepository;

    public Advertisement findByAdvertiseId(Long advertiseId) {
        return advertisementRepository.findById(advertiseId)
                .orElseThrow(() -> new EntityNotFoundException(AdvertisementError.ADVERTISEMENT_NOT_FOUND));
    }

    public void save(Advertisement advertisement) {
        advertisementRepository.save(advertisement);
    }

    public Page<AdvertisementInfo> findAdvertisements(Pageable pageable) {
        return advertisementRepository.advertisementList(pageable);
    }
}
