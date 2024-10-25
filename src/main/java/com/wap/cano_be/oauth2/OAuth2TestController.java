package com.wap.cano_be.oauth2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2TestController {
    @GetMapping("/oauth2/callback-test")
    public String callbackTest(@RequestParam("code") String code) {
        return "Authorization Code: " + code;
    }
}
