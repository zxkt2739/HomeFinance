package com.example.module1.dao;

import com.example.module1.model.dto.UserDTO;
import com.example.module1.model.entity.User;
import com.example.core.dao.BaseDAO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @package:  com.example.module1.mapper
 * @description: 用户表
 * @version: V1.0
 */
@Mapper
public interface UserDAO extends BaseDAO<User> {

    /**
     * 根据查询条件得到指定字段集合的数据列表，包含分页和排序信息
     * @param params
     * @return
     */
    List<UserDTO> selectDTO(Map<String, Object> params);

    /**
     * 根据id查询一条 UserDTO
     * @param params
     * @return
     */
    UserDTO selectOneDTO(Map<String, Object> params);

    /**
     * 根据Id更新last_login_time上次登陆时间字段
     * last_login_ip 上次登陆IP字段
     *
     * @param params 数据结构 Map<key, Map<key, value>>
     *               key:datas里放需要更新的键值对；
     *               conditions里放where条件筛选条件键值对
     * @return 影响的条数
     */
    int updateLastLoginDate(Long params);

}
