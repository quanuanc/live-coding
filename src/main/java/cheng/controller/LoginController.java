package cheng.controller;

import cheng.service.LoginService;
import cheng.vo.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public String login(Login login) {
        log.debug("username: {}", login.getUsername());
        log.debug("password: {}", login.getPassword());
        log.debug("displayName: {}", login.getDisplayName());

        loginService.login(login);

        return "OK";
    }
}
