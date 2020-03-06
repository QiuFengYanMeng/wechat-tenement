package com.study.wang.tenement.entity.house;

import lombok.Data;

/**
 *  <p>
 *      房源关注，数据实体类
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-06
 * */
@Data
public class Attention {
    //用户ID
    private int userId;
    //房源ID
    private int houseId;
}
