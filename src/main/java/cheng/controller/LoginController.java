package cheng.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @PostMapping
    public String login(Login login) {
        log.debug("username: {}", login.getUsername());
        log.debug("password: {}", login.getPassword());
        log.debug("displayName: {}", login.getDisplayName());
        return "OK";
    }

    @Data
    static class Login {
        private String username;
        private String password;
        private String displayName;
    }
}
