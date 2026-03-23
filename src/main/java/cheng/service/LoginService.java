package cheng.service;

import cheng.vo.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService {
    public void login(Login login) {
        log.info("currently user: {} is using login api", login.getUsername());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("sleep interrupted");
        }
        log.info("user: {}, password: {}", login.getUsername(), login.getPassword());
    }
}
