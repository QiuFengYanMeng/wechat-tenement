package com.study.wang.tenement.dao.user;

import com.study.wang.tenement.entity.user.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 *  <p>
 *      用户模块的数据层接口
 *  </p>
 *
 * @author 王子洋
 * @date 2020-03-05
 * */
@Mapper
public interface SysUserDao {

    /**
     *  新增用户
     * @param user 数据实体
     * */
    int add (SysUser user);
}
