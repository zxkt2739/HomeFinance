package com.example.module1.service.impl;

import com.example.core.util.I18nUtils;
import com.example.module1.dao.ProductDAO;
import com.example.module1.model.dto.ProductDTO;
import com.example.module1.model.entity.Product;
import com.example.module1.service.ProductService;
import com.example.core.common.PagingContext;
import com.example.core.common.SortingContext;
import com.example.core.service.impl.BaseServiceImpl;
import com.example.core.util.BeanUtil;
import com.example.core.exception.BizException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 
 * </p>
 *
 * @package: com.example.module1.service.impl
 * @description: 
 * @author: fenmi
 * @date: Created in 2020-03-13 15:01:03
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: fenmi
 */
@Service
@Slf4j
public class ProductServiceImpl extends BaseServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    public void saveProduct(@NonNull ProductDTO productDTO, HttpServletRequest request) throws BizException {
        Product product = BeanUtil.copyProperties(productDTO, new Product());
        log.info("save Product:{}", product);
        if (productDAO.insert((Product) this.packAddBaseProps(product, request)) != 1) {
            log.error("insert error, data:{}", product);
            throw new BizException("Insert product Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductList(@NonNull List<Product> productList, HttpServletRequest request) throws BizException {
        if (productList.size() == 0) {
            throw new BizException(I18nUtils.get("parameter.rule.length", getLang(request)));
        }
        int rows = productDAO.insertList(productList);
        if (rows != productList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, productList.size());
            throw new BizException(I18nUtils.get("batch.saving.exception", getLang(request)));
        }
    }

    @Override
    public void updateProduct(@NonNull Long id, @NonNull ProductDTO productDTO, HttpServletRequest request) throws BizException {
        log.info("full update productDTO:{}", productDTO);
        Product product = BeanUtil.copyProperties(productDTO, new Product());
        product.setId(id);
        int cnt = productDAO.update((Product) this.packModifyBaseProps(product, request));
        if (cnt != 1) {
            log.error("update error, data:{}", productDTO);
            throw new BizException("update product Error!");
        }
    }

    @Override
    public void updateProductSelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        productDAO.updatex(params);
    }

    @Override
    public void logicDeleteProduct(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("逻辑删除，数据id:{}", id);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", getUserId(request));
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = productDAO.delete(params);
        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public void deleteProduct(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = productDAO.pdelete(params);
        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public ProductDTO findProductById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        ProductDTO productDTO = productDAO.selectOneDTO(params);
        return productDTO;
    }

    @Override
    public ProductDTO findOneProduct(@NonNull Map<String, Object> params) {
        log.info("find one params:{}", params);
        Product product = productDAO.selectOne(params);
        ProductDTO productDTO = new ProductDTO();
        if (null != product) {
            BeanUtils.copyProperties(product, productDTO);
        }
        return productDTO;
    }

    @Override
    public List<ProductDTO> find(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params = getUnionParams(params, scs, pc);
        List<ProductDTO> resultList = productDAO.selectDTO(params);
        return resultList;
    }

    @Override
    public List<Map> findMap(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc, String... columns) throws BizException {
        if (columns.length == 0) {
            throw new BizException("columns长度不能为0");
        }
        params = getUnionParams(params, scs, pc);
        params.put("columns", columns);
        return productDAO.selectMap(params);
    }

    private Map<String, Object> getUnionParams(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params.put("pc", pc);
        params.put("scs", scs);
        return params;
    }

    @Override
    public int count(@NonNull Map<String, Object> params) {
        return productDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = productDAO.groupCount(conditions);
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map<String, Object> m : maps) {
            String key = m.get("group") != null ? m.get("group").toString() : "group";
            Object value = m.get("count");
            int count = 0;
            if (StringUtils.isNotBlank(value.toString())) {
                count = Integer.parseInt(value.toString());
            }
            map.put(key, count);
        }
        return map;
    }

    @Override
    public Double sum(String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("sumfield", sumField);
        return productDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = productDAO.groupSum(conditions);
        Map<String, Double> map = new LinkedHashMap<>();
        for (Map<String, Object> m : maps) {
            String key = m.get("group") != null ? m.get("group").toString() : "group";
            Object value = m.get("sum");
            double sum = 0d;
            if (StringUtils.isNotBlank(value.toString())) {
                sum = Double.parseDouble(value.toString());
            }
            map.put(key, sum);
        }
        return map;
    }

}
