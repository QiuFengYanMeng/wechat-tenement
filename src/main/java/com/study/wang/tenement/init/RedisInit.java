package com.study.wang.tenement.init;

import com.study.wang.tenement.util.AccessTokenUtil;
import com.study.wang.tenement.util.redis.Redis;
import com.study.wang.tenement.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *  <p>
 *      初始化Redis连接信息
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-04
 * */
@Component
public class RedisInit implements ApplicationRunner {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    @Value("${wechat.accessTokenUrl}")
    private String accessTokenUrl;
    //应用ID
    @Value("${wechat.appId}")
    private String appId;
    //开发者密钥
    @Value("${wechat.secret}")
    private String secret;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Redis> redisList = new ArrayList<>();
        //127.0.0.1:6379
        redisList.add(new Redis(host + ":" + port , password));
        RedisUtil.init(redisList);
        System.out.println("初始化Redis成功");

        AccessTokenUtil.accessTokenUrl =this.accessTokenUrl;
        AccessTokenUtil.appId = this.appId;
        AccessTokenUtil.secret = this.secret;
    }
}
