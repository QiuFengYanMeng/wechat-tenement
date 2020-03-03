package com.study.wang.tenement.controller;

import com.study.wang.tenement.util.MessageUtil;
import com.study.wang.tenement.util.XMLUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *  <p>
 *      与微信产生交互的控制器
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-03
 * */
@RestController
public class WechatController {

    /**
     *  与微信服务器进行认证的API接口
     * */
    @GetMapping(value = "/wechat")
    public Object check (String signature ,
                         String timestamp ,
                         String nonce ,
                         String echostr) {
        return echostr;
    }

    /**
     *  处理微信用户的交互行为
     * @param request 请求对象
     * */
    @PostMapping(value = "/wechat" , produces = {"application/xml;charset=utf-8"})
    public Object doRequest (HttpServletRequest request) throws IOException {
        //解析XML文档，判断消息类型，判断事件类型，实现沟通逻辑

        //解析XML文档，转换为Map，可读性更高，获取数据方便
        Map<String , String> map = XMLUtil.getMap(request.getInputStream());
        //获取消息类型
        String msgType = map.get("MsgType");
        //获取消息是谁发的，用户的openid
        String fromUser = map.get("FromUserName");
        //发送给谁，我们的公众号的账号
        String toUser = map.get("ToUserName");

        //要回复的内容
        String reply = "";

        //如果是事件类型的消息
        if (msgType.equals("event")) {
            //获取事件类型
            String event = map.get("Event");
            //如果是关注事件
            if (event.equals("subscribe")) {
                reply = "欢迎您关注微租房平台！";
            }
        }else if (msgType.equals("text")) {
            //用户发送给我们的消息
            String content = map.get("Content");
            if (content.equals("1")) {
                reply = "请在官网领取优惠券";
            }else if (content.equals("2")) {
                reply = "在房源详情，可直接联系经纪人";
            }else if (content.equals("3")) {
                reply = "更多帮助，请联系客服电话：8956-531";
            }else {
                reply = "请回复您要解决的问题编号：\n" +
                        "1、在哪里可以领取优惠券\n" +
                        "2、怎样联系房源的经纪人\n" +
                        "3、其他帮助";
            }
        }

        return MessageUtil.formatMsg(fromUser , toUser , 454245 , "text" , reply);
    }
}
