package kr.iam.domain.channel.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.channel.dto.req.ChannelSaveReqDto;
import kr.iam.domain.channel.dto.res.ChannelResDto;
import kr.iam.global.annotation.MemberInfo;
import kr.iam.global.aspect.member.MemberInfoParam;
import kr.iam.global.domain.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "내 정보 수정 및 입력", description = "내 정보 수정 및 입력 모두 수행 아마도?")
    @PatchMapping
    public ResponseEntity<SuccessResponse<?>> createChannelInfo(@RequestPart(value = "image", required = false) MultipartFile file,
                                                             @RequestPart("ChannelSaveRequestDto") String ChannelSaveRequestDtoString,
                                                             @MemberInfo MemberInfoParam memberInfoParam)
            throws IOException {
        ChannelSaveReqDto channelSaveRequestDto = objectMapper.readValue(ChannelSaveRequestDtoString, ChannelSaveReqDto.class);

        channelService.updateInfo(file, channelSaveRequestDto, memberInfoParam);
        return SuccessResponse.ok(null);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getChannelInfo(@MemberInfo MemberInfoParam memberInfoParam) {
        return SuccessResponse.ok(channelService.getInfo(memberInfoParam));
    }
}
