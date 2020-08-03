package com.example.module1.dao;

import com.example.module1.model.dto.ExpendDTO;
import com.example.module1.model.entity.Expend;
import com.example.core.dao.BaseDAO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

import java.util.List;

/**
 * <p>
 * 支出明细表
 * </p>
 *
 * @package:  com.example.module1.mapper
 * @description: 支出明细表
 * @version: V1.0
 */
@Mapper
public interface ExpendDAO extends BaseDAO<Expend> {

    /**
     * 根据查询条件得到指定字段集合的数据列表，包含分页和排序信息
     * @param params
     * @return
     */
    List<ExpendDTO> selectDTO(Map<String, Object> params);

    /**
     * 根据id查询一条 ExpendDTO
     * @param params
     * @return
     */
    ExpendDTO selectOneDTO(Map<String, Object> params);

}
