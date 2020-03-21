package com.example.module1.dao;

import com.example.module1.model.dto.ProductDTO;
import com.example.module1.model.entity.Product;
import com.example.core.dao.BaseDAO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @package:  com.example.module1.mapper
 * @description: 
 * @author: fenmi
 * @date: Created in 2020-03-13 15:01:03
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: fenmi
 */
@Mapper
public interface ProductDAO extends BaseDAO<Product> {

    /**
     * 根据查询条件得到指定字段集合的数据列表，包含分页和排序信息
     * @param params
     * @return
     */
    List<ProductDTO> selectDTO(Map<String, Object> params);

    /**
     * 根据id查询一条 ProductDTO
     * @param params
     * @return
     */
    ProductDTO selectOneDTO(Map<String, Object> params);

}
