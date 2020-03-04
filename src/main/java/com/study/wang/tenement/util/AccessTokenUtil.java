package com.study.wang.tenement.util;

import com.alibaba.fastjson.JSON;
import com.study.wang.tenement.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;

/**
 *  <p>
 *      获取微信的token工具类
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-04
 * */
@Slf4j
public class AccessTokenUtil {

    //获取access_token的接口地址
    public static String accessTokenUrl;
    //应用ID
    public static String appId;
    //开发者密钥
    public static String secret;

    /**
     *  获取token
     * */
    public static String getToken () {
        //先查看Redis当中有没有，如果没有，就请求微信的接口，同时存储到Redis，同时设置过期时间
        String token = RedisUtil.getStringValue("wechat_token");
        if (token == null) {
            //Redis没有，请求微信的接口
            try {
                //1、客户端
                OkHttpClient client = new OkHttpClient();
                //2、构建Request请求对象
                Request request = new Request.Builder().url(accessTokenUrl + "?grant_type=client_credential&appid=" + appId + "&secret=" + secret).build();
                //3、请求接口，拿到响应信息
                Response response = client.newCall(request).execute();
                //处理返回的结果
                if (response.isSuccessful()) {
                    //拿到响应结果
                    String body = response.body().string();
                    //拿到响应回来的token
                    token = JSON.parseObject(body).getString("access_token");
                    //存放到Redis
                    RedisUtil.setStringValue("wechat_token" , token , 7100);
                }
            }catch (Exception e) {
                log.error("获取token时异常：" + e);
            }

        }
        return token;
    }
}
