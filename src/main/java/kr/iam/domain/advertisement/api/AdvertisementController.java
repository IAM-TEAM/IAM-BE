package kr.iam.domain.advertisement.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.advertisement.application.AdvertisementService;
import kr.iam.domain.advertisement.dto.AdvertisementDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static kr.iam.domain.advertisement.dto.AdvertisementDto.*;

@Slf4j
@RestController
@RequestMapping("/advertisement")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<String> enrollAdvertisement(@RequestPart String enrollAdvertisementDto,
                                                      @RequestPart(required = false) MultipartFile file,
                                                      HttpServletRequest request) throws IOException {
        EnrollAdvertisementDto enrollAdvertisementDtoContent =
                objectMapper.readValue(enrollAdvertisementDto, EnrollAdvertisementDto.class);

        Long saveId = advertisementService.enrollAdvertisement(file, enrollAdvertisementDtoContent, request);
        return ResponseEntity.ok(saveId + " created");
    }
}
