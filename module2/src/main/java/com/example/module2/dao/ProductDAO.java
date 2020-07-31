package com.example.module2.dao;

import com.example.core.dao.BaseDAO;
import com.example.module2.model.dto.ProductDTO;
import com.example.module2.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
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
