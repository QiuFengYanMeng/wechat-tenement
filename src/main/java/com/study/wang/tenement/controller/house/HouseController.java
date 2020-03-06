package com.study.wang.tenement.controller.house;

import com.study.wang.tenement.back.Back;
import com.study.wang.tenement.entity.house.Attention;
import com.study.wang.tenement.entity.house.House;
import com.study.wang.tenement.service.house.HouseService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 *  <p>
 *      房源模块的控制层组件
 *  </p>
 * @author 王子洋
 * @date 2020-03-05
 * */
@RestController
@RequestMapping(value = "/tenement")
@AllArgsConstructor
public class HouseController {

    private HouseService houseService;

    /**
     *  新增房源
     * @param house 数据实体
     * @return 受影响的行数
     * */
    @PostMapping(value = "add")
    @Transactional(rollbackFor = Exception.class)
    public Back add (@RequestBody House house) {
        return houseService.add(house);
    }

    /**
     *  获取房源列表
     * @param start 分页起始游标
     * @param limit 分页的大小
     * @return HouseVO集合
     * */
    @GetMapping(value = "/list")
    public Back listHouse (int page , int limit) {
        return houseService.listHouse(page, limit);
    }

    /**
     *  获取房源详情
     * @param id 房源ID
     * @return DetailVO对象
     * */
    @GetMapping(value = "/detail/{id}")
    public Back getHouse (@PathVariable int id) {
        return houseService.getHouse(id);
    }

    /**
     *  关注房源
     * @param attention 数据实体
     * */
    @PostMapping(value = "/attention")
    public Back addAttention (@RequestBody Attention attention) {
        return houseService.addAttention(attention);
    }
}
