package com.baseurak.AFDay.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 일반 로그인 관련 요청을 HTTP 프로토콜로 받아 처리합니다.
 * @Author: Uju, Ru
 */
@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * GET - /user 요청 시 로그인한 사용자를 저장합니다.
     */
    @GetMapping("/user")
    public User findUser() {
        return userService.findUserBySessionId();
    }

    //@GetMapping("/user")
    public List<User> readUserList(){
        return userService.readUserList();
    }

    /**
     * DELETE - /user  요청 시 로그인한 사용자를 삭제합니다.
     */
    @DeleteMapping("/user")
    public void delete(){
        userService.delete();
    }

    /**
     * PUT - /name 요청 시 로그인한 사용자 이름을 name으로  변경합니다.
     * @Param String string: 사용자 이름
     */
    @PutMapping("/name")
    public void modifyName(String name) {
        userService.modifyName(name, userService.findUserBySessionId());
    }
}
