package com.study.wang.tenement.entity.house;

import lombok.Data;

/**
 *  <p>
 *      房源标签
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-05
 * */
@Data
public class HouseLabel {
    //主键ID
    private Integer id;
    //房源ID
    private Integer houseId;
    //标签
    private String title;
}
