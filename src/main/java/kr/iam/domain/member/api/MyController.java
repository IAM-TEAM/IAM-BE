package kr.iam.domain.member.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class MyController {
    @GetMapping("/my")
    public String adminP(){
        return "my Controller";
    }
}
