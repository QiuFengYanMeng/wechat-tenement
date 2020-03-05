package com.study.wang.tenement.entity.house;

import com.study.wang.tenement.base.house.HouseBase;
import com.study.wang.tenement.entity.user.SysUser;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *  <p>
 *      房源实体类
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-05
 * */
@Data
public class House extends HouseBase {
    //房间面积
    private Integer houseArea;
    //楼层
    private String floor;
    //装修
    private String fitment;
    //供暖
    private String heating;
    //年代
    private String houseYear;
    //楼层类型
    private String floorType;
    //经纪人房评
    private String comment;
    //创建人
    private SysUser createUser;
}
