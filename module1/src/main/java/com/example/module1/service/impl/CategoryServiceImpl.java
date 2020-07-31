package com.example.module1.service.impl;

import com.example.core.util.I18nUtils;
import com.example.module1.dao.CategoryDAO;
import com.example.module1.model.dto.CategoryDTO;
import com.example.module1.model.entity.Category;
import com.example.module1.service.CategoryService;
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
 * 主分类表
 * </p>
 *
 * @package: com.example.module1.service.impl
 * @description: 主分类表
 * @author: fenmi
 * @date: Created in 2020-07-31 16:00:06
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: fenmi
 */
@Service
@Slf4j
public class CategoryServiceImpl extends BaseServiceImpl implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public void saveCategory(@NonNull CategoryDTO categoryDTO, HttpServletRequest request) throws BizException {
        Category category = BeanUtil.copyProperties(categoryDTO, new Category());
        log.info("save Category:{}", category);
        if (categoryDAO.insert((Category) this.packAddBaseProps(category, request)) != 1) {
            log.error("insert error, data:{}", category);
            throw new BizException("Insert category Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCategoryList(@NonNull List<Category> categoryList, HttpServletRequest request) throws BizException {
        if (categoryList.size() == 0) {
            throw new BizException(I18nUtils.get("parameter.rule.length", getLang(request)));
        }
        int rows = categoryDAO.insertList(categoryList);
        if (rows != categoryList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, categoryList.size());
            throw new BizException(I18nUtils.get("batch.saving.exception", getLang(request)));
        }
    }

    @Override
    public void updateCategory(@NonNull Long id, @NonNull CategoryDTO categoryDTO, HttpServletRequest request) throws BizException {
        log.info("full update categoryDTO:{}", categoryDTO);
        Category category = BeanUtil.copyProperties(categoryDTO, new Category());
        category.setId(id);
        int cnt = categoryDAO.update((Category) this.packModifyBaseProps(category, request));
        if (cnt != 1) {
            log.error("update error, data:{}", categoryDTO);
            throw new BizException("update category Error!");
        }
    }

    @Override
    public void updateCategorySelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        categoryDAO.updatex(params);
    }

    @Override
    public void logicDeleteCategory(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("逻辑删除，数据id:{}", id);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", getUserId(request));
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = categoryDAO.delete(params);
        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public void deleteCategory(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = categoryDAO.pdelete(params);
        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public CategoryDTO findCategoryById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        CategoryDTO categoryDTO = categoryDAO.selectOneDTO(params);
        return categoryDTO;
    }

    @Override
    public CategoryDTO findOneCategory(@NonNull Map<String, Object> params) {
        log.info("find one params:{}", params);
        Category category = categoryDAO.selectOne(params);
        CategoryDTO categoryDTO = new CategoryDTO();
        if (null != category) {
            BeanUtils.copyProperties(category, categoryDTO);
        }
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> find(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params = getUnionParams(params, scs, pc);
        List<CategoryDTO> resultList = categoryDAO.selectDTO(params);
        return resultList;
    }

    @Override
    public List<Map> findMap(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc, String... columns) throws BizException {
        if (columns.length == 0) {
            throw new BizException("columns长度不能为0");
        }
        params = getUnionParams(params, scs, pc);
        params.put("columns", columns);
        return categoryDAO.selectMap(params);
    }

    private Map<String, Object> getUnionParams(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params.put("pc", pc);
        params.put("scs", scs);
        return params;
    }

    @Override
    public int count(@NonNull Map<String, Object> params) {
        return categoryDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = categoryDAO.groupCount(conditions);
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
        return categoryDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = categoryDAO.groupSum(conditions);
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
