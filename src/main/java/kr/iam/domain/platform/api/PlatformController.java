package kr.iam.domain.platform.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.iam.domain.platform.application.PlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/platform")
@RequiredArgsConstructor
public class PlatformController {

    private final PlatformService platformService;

    @Operation(summary = "플렛폼 조회", description = "플랫폼 Id 기반으로 플랫폼 하이퍼링크? 이동")
    @GetMapping("/{platformId}")
    public String getPlatform(@PathVariable Long platformId){
        return platformService.getPlatform(platformId);
    }
}
