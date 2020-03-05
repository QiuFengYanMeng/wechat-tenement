package com.study.wang.tenement.controller.wechat;

import com.study.wang.tenement.queue.user.UserQueue;
import com.study.wang.tenement.util.AccessTokenUtil;
import com.study.wang.tenement.util.MessageUtil;
import com.study.wang.tenement.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class WechatController {

    @Value("${wechat.menuUrl}")
    private String menuUrl;

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
                //在这里实现，获取用户信息，并且信息入库
                //放入阻塞队列
                UserQueue.QUEUE.push(fromUser);
            }else if (event.equals("CLICK")) {
                //如果是点击事件，获取菜单的key值，实现我们自己的业务逻辑
                String key = map.get("EventKey");
                if (key.equals("get-post")) {
                    reply = "功能正在建设中，请期待...";
                }
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

    /**
     *  创建菜单
     * */
    @GetMapping(value = "/create-menu")
    public Object test () {
        String token = AccessTokenUtil.getToken();
        String paramStr = "{\n" +
                "  \"button\":[\n" +
                "    {\n" +
                "      \"type\":\"click\",\n" +
                "      \"name\":\"我的海报\",\n" +
                "      \"key\":\"get-post\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"智慧服务\",\n" +
                "      \"sub_button\":[\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"平台公告\",\n" +
                "          \"url\":\"https://news.qq.com/\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"屌丝有话说\",\n" +
                "          \"url\":\"https://news.163.com/\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"智享搜搜乐\",\n" +
                "          \"url\":\"https://www.baidu.com/index.php?tn=monline_3_dg\"\n" +
                "        }]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"海量房源\",\n" +
                "      \"type\":\"view\",\n" +
                "      \"url\":\"http://vaf5vc.natappfree.cc/index.html\"\n" +
                "    }]\n" +
                "}\n";


        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8") , paramStr);
            Request request = new Request.Builder().url(menuUrl + "?access_token=" + token).post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("创建菜单成功");
            }
        }catch (Exception e) {
            log.error("创建菜单出错");
        }

        return "success";
    }
}
