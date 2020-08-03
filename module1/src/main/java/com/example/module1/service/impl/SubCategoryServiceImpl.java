package com.example.module1.service.impl;

import com.example.core.util.I18nUtils;
import com.example.module1.dao.SubCategoryDAO;
import com.example.module1.model.dto.SubCategoryDTO;
import com.example.module1.model.entity.SubCategory;
import com.example.module1.service.SubCategoryService;
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
 * 子分类表
 * </p>
 *
 * @package: com.example.module1.service.impl
 * @description: 子分类表
 * @version: V1.0
 */
@Service
@Slf4j
public class SubCategoryServiceImpl extends BaseServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryDAO subCategoryDAO;

    @Override
    public void saveSubCategory(@NonNull SubCategoryDTO subCategoryDTO, HttpServletRequest request) throws BizException {
        SubCategory subCategory = BeanUtil.copyProperties(subCategoryDTO, new SubCategory());
        log.info("save SubCategory:{}", subCategory);
        if (subCategoryDAO.insert((SubCategory) this.packAddBaseProps(subCategory, request)) != 1) {
            log.error("insert error, data:{}", subCategory);
            throw new BizException("Insert subCategory Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSubCategoryList(@NonNull List<SubCategory> subCategoryList, HttpServletRequest request) throws BizException {
        if (subCategoryList.size() == 0) {
            throw new BizException(I18nUtils.get("parameter.rule.length", getLang(request)));
        }
        int rows = subCategoryDAO.insertList(subCategoryList);
        if (rows != subCategoryList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, subCategoryList.size());
            throw new BizException(I18nUtils.get("batch.saving.exception", getLang(request)));
        }
    }

    @Override
    public void updateSubCategory(@NonNull Long id, @NonNull SubCategoryDTO subCategoryDTO, HttpServletRequest request) throws BizException {
        log.info("full update subCategoryDTO:{}", subCategoryDTO);
        SubCategory subCategory = BeanUtil.copyProperties(subCategoryDTO, new SubCategory());
        subCategory.setId(id);
        int cnt = subCategoryDAO.update((SubCategory) this.packModifyBaseProps(subCategory, request));
        if (cnt != 1) {
            log.error("update error, data:{}", subCategoryDTO);
            throw new BizException("update subCategory Error!");
        }
    }

    @Override
    public void updateSubCategorySelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        subCategoryDAO.updatex(params);
    }

    @Override
    public void logicDeleteSubCategory(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("逻辑删除，数据id:{}", id);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", getUserId(request));
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = subCategoryDAO.delete(params);
        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public void deleteSubCategory(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = subCategoryDAO.pdelete(params);
        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public SubCategoryDTO findSubCategoryById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        SubCategoryDTO subCategoryDTO = subCategoryDAO.selectOneDTO(params);
        return subCategoryDTO;
    }

    @Override
    public SubCategoryDTO findOneSubCategory(@NonNull Map<String, Object> params) {
        log.info("find one params:{}", params);
        SubCategory subCategory = subCategoryDAO.selectOne(params);
        SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
        if (null != subCategory) {
            BeanUtils.copyProperties(subCategory, subCategoryDTO);
        }
        return subCategoryDTO;
    }

    @Override
    public List<SubCategoryDTO> find(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params = getUnionParams(params, scs, pc);
        List<SubCategoryDTO> resultList = subCategoryDAO.selectDTO(params);
        return resultList;
    }

    @Override
    public List<Map> findMap(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc, String... columns) throws BizException {
        if (columns.length == 0) {
            throw new BizException("columns长度不能为0");
        }
        params = getUnionParams(params, scs, pc);
        params.put("columns", columns);
        return subCategoryDAO.selectMap(params);
    }

    private Map<String, Object> getUnionParams(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params.put("pc", pc);
        params.put("scs", scs);
        return params;
    }

    @Override
    public int count(@NonNull Map<String, Object> params) {
        return subCategoryDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = subCategoryDAO.groupCount(conditions);
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
        return subCategoryDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = subCategoryDAO.groupSum(conditions);
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
