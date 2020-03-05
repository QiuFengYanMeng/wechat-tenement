package com.study.wang.tenement.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.study.wang.tenement.back.Back;
import com.study.wang.tenement.entity.user.SysUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 *  <p>
 *      用户模块的服务层组件
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-05
 * */
@Service
public class UserService {

    //网页授权获取access_token的地址
    @Value("${wechat.loginTokenUrl}")
    private String loginTokenUrl;
    //应用ID
    @Value("${wechat.appId}")
    private String appId;
    //开发者密钥
    @Value("${wechat.secret}")
    private String secret;
    //换取用户信息的接口
    @Value("${wechat.loginInfoUrl}")
    private String loginInfoUrl;

    /**
     *  微信登录接口
     * @param code 前端获取到的code
     * @return 返回用户的信息
     * */
    public Back<SysUser> login (String code) throws IOException {
        //最后要返回的user对象
        SysUser user = new SysUser();

        //客户端
        OkHttpClient client = new OkHttpClient();
        //构建的Request
        Request request = new
                Request.Builder()
                .url(loginTokenUrl + "?appid=" + appId + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code").build();
        //得到响应信息
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            //响应体
            String body = response.body().string();
            //转换为JSON对象
            JSONObject object = JSON.parseObject(body);
            //提取授权access_token
            String accessToken = object.getString("access_token");
            //提取openid
            String openId = object.getString("openid");

            //请求获取用户信息的接口
            request = new Request.Builder().url(loginInfoUrl + "?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN").build();
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                body = response.body().string();
                object = JSON.parseObject(body);

                //设置用户名
                user.setUsername(object.getString("nickname"));
                user.setImgSrc(object.getString("headimgurl"));

                //异步队列，检验用户是否在数据库中，以及写入数据库，省略实现。。。。
            }
        }
        return new Back<>(user).msg("success");
    }
}
