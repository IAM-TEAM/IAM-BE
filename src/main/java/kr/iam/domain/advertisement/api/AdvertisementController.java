package kr.iam.domain.advertisement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.advertisement.application.AdvertisementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static kr.iam.domain.advertisement.dto.AdvertisementDto.AdvertisementResDto;
import static kr.iam.domain.advertisement.dto.AdvertisementDto.EnrollAdvertisementDto;

@Slf4j
@RestController
@RequestMapping("/advertisement")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "광고 생성", description = "광고(관리자 용 default channel 1) 생성 + file(이미지)")
    @PostMapping
    public ResponseEntity<String> enrollAdvertisement(@RequestPart("enrollAdvertisementDto")
                                                          EnrollAdvertisementDto enrollAdvertisementDto,
                                                      @RequestPart(required = false) MultipartFile file,
                                                      HttpServletRequest request) throws IOException {
//        EnrollAdvertisementDto enrollAdvertisementDtoContent =
//                objectMapper.readValue(enrollAdvertisementDto, EnrollAdvertisementDto.class);

        Long saveId = advertisementService.enrollAdvertisement(file, enrollAdvertisementDto, request);
        return ResponseEntity.ok(saveId + " created");
    }

    @GetMapping
    public ResponseEntity<AdvertisementResDto> getAdvertisementInfos(Pageable pageable) {
        return ResponseEntity.ok(advertisementService.getAdvertisements(pageable));
    }
}
