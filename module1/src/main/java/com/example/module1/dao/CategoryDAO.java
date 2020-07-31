package com.example.module1.dao;

import com.example.module1.model.dto.CategoryDTO;
import com.example.module1.model.entity.Category;
import com.example.core.dao.BaseDAO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

import java.util.List;

/**
 * <p>
 * 主分类表
 * </p>
 *
 * @package:  com.example.module1.mapper
 * @description: 主分类表
 * @author: fenmi
 * @date: Created in 2020-07-31 15:59:49
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: fenmi
 */
@Mapper
public interface CategoryDAO extends BaseDAO<Category> {

    /**
     * 根据查询条件得到指定字段集合的数据列表，包含分页和排序信息
     * @param params
     * @return
     */
    List<CategoryDTO> selectDTO(Map<String, Object> params);

    /**
     * 根据id查询一条 CategoryDTO
     * @param params
     * @return
     */
    CategoryDTO selectOneDTO(Map<String, Object> params);

}
