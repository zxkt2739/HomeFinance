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

}
