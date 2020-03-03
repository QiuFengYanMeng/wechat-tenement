package com.study.wang.tenement.util;

/**
 *  <p>
 *      与微信用户发送消息的工具类
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-03
 * */
public class MessageUtil {

    /**
     *  格式化XML消息
     * @param toUser 接收人的openid
     * @param fromUser 发送人的账号
     * @param createTime 消息创建时间
     * @param msgType 消息类型
     * @param content 消息内容
     * */
    public static String formatMsg (String toUser , String fromUser , int createTime , String msgType , String content) {
        String str = "<xml>\n" +
                "  <ToUserName><![CDATA[%s]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[%s]]></FromUserName>\n" +
                "  <CreateTime>%d</CreateTime>\n" +
                "  <MsgType><![CDATA[%s]]></MsgType>\n" +
                "  <Content><![CDATA[%s]]></Content>\n" +
                "</xml>\n" +
                "\n";

        return String.format(str , toUser , fromUser , createTime , msgType , content);
    }
}
