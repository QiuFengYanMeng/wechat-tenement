package com.study.wang.tenement.queue.user;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.study.wang.tenement.dao.user.SysUserDao;
import com.study.wang.tenement.entity.user.SysUser;
import com.study.wang.tenement.util.AccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
public class UserQueue {

    //存放用户的openid的阻塞队列
    public static final BlockingDeque<String> QUEUE = new LinkedBlockingDeque<>();
    //监听阻塞队列的线程数量
    private static final int THREAD_COUNT = 10;
    //获取用户信息的接口地址（通过UID机制）
    public static String userInfoUrl;
    //数据库层组件
    public static SysUserDao userDao;

    /**
     *  监听阻塞队列，异步执行任务
     * */
    public static void listen () {
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            //从队列拉取数据
                            String openId = QUEUE.take();
                            //access_token
                            String accessToken = AccessTokenUtil.getToken();
                            //声明客户端
                            OkHttpClient client = new OkHttpClient();
                            //构建Request
                            Request request = new Request.Builder().url(userInfoUrl + "?access_token=" + accessToken + "&openid=" + openId +
                                                 "&lang=zh_CN").build();
                            Response response = client.newCall(request).execute();
                            //如果请求成功，解析数据
                            if (response.isSuccessful()) {
                                String body = response.body().string();
                                //得到一个JSON对象
                                JSONObject object = JSON.parseObject(body);
                                //声明系统用户
                                SysUser user = new SysUser();
                                user.setUsername(object.getString("nickname"));
                                user.setImgSrc(object.getString("headimgurl"));
                                user.setOpenId(object.getString("openid"));
                                //信息入库
                                userDao.add(user);
                            }
                        }
                    }catch (Exception e) {
                        log.error("异步处理业务时失败..." + e);
                    }
                }
            });
            thread.start();
        }
        System.out.println("开始监听。。。");
    }
}
