package kr.iam.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class testController {

    @GetMapping("/test")
    public String mainP() {

        return "TEST Controller";
    }
}
