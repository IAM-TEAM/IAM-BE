package kr.iam.domain.channel.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @PatchMapping
    public ResponseEntity<String> createChannelInfo(@RequestPart("image") MultipartFile file,
                                                    @RequestPart("ChannelSaveRequestDto") String ChannelSaveRequestDtoString,
                                                    HttpServletRequest request)
            throws IOException {
        ChannelSaveRequestDto channelSaveRequestDto = objectMapper.readValue(ChannelSaveRequestDtoString, ChannelSaveRequestDto.class);

        channelService.updateInfo(file, channelSaveRequestDto, request);
        return ResponseEntity.ok("Updated Okay");
    }
}
