package com.study.wang.tenement.entity.user;

import lombok.Data;

/**
 *  <p>
 *      用户实体类
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-05
 * */
@Data
public class SysUser {
    //用户ID
    private Integer id;
    //用户名
    private String username;
    //联系方式
    private String phone;
    //微信的OpenID
    private String openId;
    //头像地址
    private String imgSrc;
}
