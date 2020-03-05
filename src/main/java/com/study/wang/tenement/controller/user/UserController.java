package com.study.wang.tenement.controller.user;

import com.study.wang.tenement.back.Back;
import com.study.wang.tenement.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 *  <p>
 *      用户模块的控制器
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-05
 * */
@RestController
@RequestMapping(value = "/tenement/user")
@AllArgsConstructor
public class UserController {

    private UserService service;

    /**
     *  微信登录接口
     * @param code 前端获取到的code
     * @return 返回用户的信息
     * */
    @GetMapping(value = "/login")
    public Back login (String code) throws IOException {
        return service.login(code);
    }
}
