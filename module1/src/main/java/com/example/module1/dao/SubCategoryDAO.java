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
 * @version: V1.0
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

    /**
     * 逻辑删除
     *
     * @param params 筛选条件的键值对
     * @return 影响的条数
     */
    int deleteByParams(Map<String, Object> params);

}
