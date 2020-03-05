package com.study.wang.tenement.base.house;

import com.study.wang.tenement.entity.house.HouseLabel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *  <p>
 *      房源实体类、VO的共用字段
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-05
 * */
@Data
public class HouseBase {
    //房源ID
    private Integer id;
    //房源图片
    private String imgSrc;
    //房源的标题
    private String title;
    //房源类型
    private String houseType;
    //朝向
    private String orientation;
    //总价
    private BigDecimal sumPrice;
    //单价
    private BigDecimal unitPrice;
    //所属的社区、小区
    private String community;
    //标签集合
    private List<HouseLabel> labelList;
}
