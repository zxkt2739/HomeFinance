package com.example.module1.dao;

import com.example.module1.model.dto.SubCategoryDTO;
import com.example.module1.model.entity.SubCategory;
import com.example.core.dao.BaseDAO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

import java.util.List;

/**
 * <p>
 * 子分类表
 * </p>
 *
 * @package:  com.example.module1.mapper
 * @description: 子分类表
 * @author: fenmi
 * @date: Created in 2020-07-31 15:59:48
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: fenmi
 */
@Mapper
public interface SubCategoryDAO extends BaseDAO<SubCategory> {

    /**
     * 根据查询条件得到指定字段集合的数据列表，包含分页和排序信息
     * @param params
     * @return
     */
    List<SubCategoryDTO> selectDTO(Map<String, Object> params);

    /**
     * 根据id查询一条 SubCategoryDTO
     * @param params
     * @return
     */
    SubCategoryDTO selectOneDTO(Map<String, Object> params);

}
