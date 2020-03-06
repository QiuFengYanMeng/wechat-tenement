package com.study.wang.tenement.service.house;

import com.study.wang.tenement.back.Back;
import com.study.wang.tenement.dao.house.HouseDao;
import com.study.wang.tenement.entity.house.Attention;
import com.study.wang.tenement.entity.house.House;
import com.study.wang.tenement.util.redis.RedisUtil;
import com.study.wang.tenement.vo.house.DetailVO;
import com.study.wang.tenement.vo.house.HouseVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *  <p>
 *      房源模块的服务层组件
 *  </p>
 * @author 王子洋
 * @date 2020-03-05
 * */
@Service
@AllArgsConstructor
public class HouseService {
    //装载依赖
    private HouseDao houseDao;

    /**
     *  新增房源
     * @param house 数据实体
     * @return 受影响的行数
     * */
    public Back add (House house) {
        //先写入房源表
        houseDao.add(house);

        if (house.getLabelList().size() != 0) {
            //再写入标签表
            houseDao.addLabel(house.getId() , house.getLabelList());
        }
        return new Back().msg("新增成功");
    }

    /**
     *  获取房源列表
     * @param start 分页起始游标
     * @param limit 分页的大小
     * @return HouseVO集合
     * */
    public Back<List<HouseVO>> listHouse (int page , int limit) {
        //设置分页游标
        page = (page - 1) * limit;
        return new Back<>(houseDao.listHouse(page, limit)).msg("请求成功");
    }

    /**
     *  获取房源详情
     * @param id 房源ID
     * @return DetailVO对象
     * */
    public Back<DetailVO> getHouse (int id) {
        return new Back<>(houseDao.getHouse(id)).msg("请求成功");
    }

    /**
     *  关注房源
     * @param attention 数据实体
     * */
    public Back addAttention (Attention attention) {
        if (houseDao.check(attention) != 0) {
            return new Back().error("您已经关注过");
        }

        houseDao.addAttention(attention);
        return new Back().msg("关注成功");
    }
}
