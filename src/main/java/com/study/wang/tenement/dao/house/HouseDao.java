package com.study.wang.tenement.dao.house;

import com.study.wang.tenement.entity.house.House;
import com.study.wang.tenement.entity.house.HouseLabel;
import com.study.wang.tenement.vo.house.DetailVO;
import com.study.wang.tenement.vo.house.HouseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  <p>
 *      房源模块，数据层组件
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-05
 * */
@Mapper
public interface HouseDao {

    /**
     *  新增房源
     * @param house 数据实体
     * @return 受影响的行数
     * */
    int add (House house);

    /**
     *  绑定标签，新增标签
     * @param houseId 房源ID
     * @param labelList 房源的标签数组
     * @return 受影响的行数
     * */
    int addLabel (@Param("houseId") int houseId , @Param("labelList")List<HouseLabel> labelList);

    /**
     *  获取房源列表
     * @param start 分页起始游标
     * @param limit 分页的大小
     * @return HouseVO集合
     * */
    List<HouseVO> listHouse (@Param("start") int start , @Param("limit") int limit);

    /**
     *  获取房源详情
     * @param id 房源ID
     * @return DetailVO对象
     * */
    DetailVO getHouse (int id);
}
