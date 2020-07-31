package com.example.module1.dao;

import com.example.module1.model.dto.IncomeDTO;
import com.example.module1.model.entity.Income;
import com.example.core.dao.BaseDAO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

import java.util.List;

/**
 * <p>
 * 收入明细表
 * </p>
 *
 * @package:  com.example.module1.mapper
 * @description: 收入明细表
 * @author: fenmi
 * @date: Created in 2020-07-31 15:59:48
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: fenmi
 */
@Mapper
public interface IncomeDAO extends BaseDAO<Income> {

    /**
     * 根据查询条件得到指定字段集合的数据列表，包含分页和排序信息
     * @param params
     * @return
     */
    List<IncomeDTO> selectDTO(Map<String, Object> params);

    /**
     * 根据id查询一条 IncomeDTO
     * @param params
     * @return
     */
    IncomeDTO selectOneDTO(Map<String, Object> params);

}
