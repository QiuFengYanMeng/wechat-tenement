package com.study.wang.tenement.controller;

import com.study.wang.tenement.util.MessageUtil;
import com.study.wang.tenement.util.XMLUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
     *  提供微信服务器进行验证的 API接口
     * */
    @GetMapping(value = "/wechat")
    public Object check (String signature ,
                         String timestamp ,
                         String nonce ,
                         String echostr) {
        //省略验证逻辑，直接返回字符串
        return echostr;
    }

    /**
     *  处理与微信用户的交互
     * @param request 请求对象
     * @return 处理结果
     * */
    @PostMapping(value = "/wechat" , produces = {"application/xml;charset=utf-8"})
    public Object doRequest (HttpServletRequest request) throws IOException {
        //获取微信推送的XML消息
        Map<String , String> map = XMLUtil.getMap(request.getInputStream());
        //消息类型
        String msgType = map.get("MsgType");
        //我们的公众号的id、编号
        String toUser = map.get("ToUserName");
        //发送人的openid
        String fromUser = map.get("FromUserName");

        //要返回的消息
        String reply = "";
        if (msgType.equals("text")) {
            //如果是文本类型，返回给用户消息
            String content = map.get("Content");
            if (content.equals("1")) {
                reply = "您好，是有的";
            }else if (content.equals("2")) {
                reply = "在房源详情页面，您可以直接联系中介";
            }else if (content.equals("3")) {
                reply = "请联系客服进行答疑：400-666666";
            }else {
                reply = "请回复您需要解答的问题编号：\n" +
                        "1、租房有没有优惠券\n" +
                        "2、如何联系中介\n" +
                        "3、其他问题";
            }
        }else if (msgType.equals("event")) {
            //事件类型
            String eventType = map.get("Event");
            if (eventType.equals("subscribe")) {
                reply = "欢迎关注微租房平台！";
            }
        }

        return MessageUtil.formatMsg(fromUser , toUser , 545134 , "text" , reply);
    }
}
