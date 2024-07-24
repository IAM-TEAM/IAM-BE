package kr.iam.domain.channel.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.channel.dto.ChannelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static kr.iam.domain.channel.dto.ChannelDto.*;

@Slf4j
@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "내 정보 수정 및 입력", description = "내 정보 수정 및 입력 모두 수행 아마도?")
    @PatchMapping
    public ResponseEntity<String> createChannelInfo(@RequestPart("image") MultipartFile file,
                                                    @RequestPart("ChannelSaveRequestDto") String ChannelSaveRequestDtoString,
                                                    HttpServletRequest request)
            throws IOException {
        ChannelSaveRequestDto channelSaveRequestDto = objectMapper.readValue(ChannelSaveRequestDtoString, ChannelSaveRequestDto.class);

        channelService.updateInfo(file, channelSaveRequestDto, request);
        return ResponseEntity.ok("Updated Okay");
    }

    //@Operation(summary = "광고 생성", description = "광고(관리자 용 default channel 1) 생성 + file(이미지)")
    @GetMapping
    public ResponseEntity<ChannelResponseDto> getChannelInfo(HttpServletRequest request) {
        return ResponseEntity.ok(channelService.getInfo(request));
    }
}
