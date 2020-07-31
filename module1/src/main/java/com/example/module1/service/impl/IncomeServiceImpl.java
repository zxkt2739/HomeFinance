package com.example.module1.service.impl;

import com.example.core.util.I18nUtils;
import com.example.module1.dao.IncomeDAO;
import com.example.module1.model.dto.IncomeDTO;
import com.example.module1.model.entity.Income;
import com.example.module1.service.IncomeService;
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
 * 收入明细表
 * </p>
 *
 * @package: com.example.module1.service.impl
 * @description: 收入明细表
 * @author: fenmi
 * @date: Created in 2020-07-31 16:00:04
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: fenmi
 */
@Service
@Slf4j
public class IncomeServiceImpl extends BaseServiceImpl implements IncomeService {

    @Autowired
    private IncomeDAO incomeDAO;

    @Override
    public void saveIncome(@NonNull IncomeDTO incomeDTO, HttpServletRequest request) throws BizException {
        Income income = BeanUtil.copyProperties(incomeDTO, new Income());
        log.info("save Income:{}", income);
        if (incomeDAO.insert((Income) this.packAddBaseProps(income, request)) != 1) {
            log.error("insert error, data:{}", income);
            throw new BizException("Insert income Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveIncomeList(@NonNull List<Income> incomeList, HttpServletRequest request) throws BizException {
        if (incomeList.size() == 0) {
            throw new BizException(I18nUtils.get("parameter.rule.length", getLang(request)));
        }
        int rows = incomeDAO.insertList(incomeList);
        if (rows != incomeList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, incomeList.size());
            throw new BizException(I18nUtils.get("batch.saving.exception", getLang(request)));
        }
    }

    @Override
    public void updateIncome(@NonNull Long id, @NonNull IncomeDTO incomeDTO, HttpServletRequest request) throws BizException {
        log.info("full update incomeDTO:{}", incomeDTO);
        Income income = BeanUtil.copyProperties(incomeDTO, new Income());
        income.setId(id);
        int cnt = incomeDAO.update((Income) this.packModifyBaseProps(income, request));
        if (cnt != 1) {
            log.error("update error, data:{}", incomeDTO);
            throw new BizException("update income Error!");
        }
    }

    @Override
    public void updateIncomeSelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        incomeDAO.updatex(params);
    }

    @Override
    public void logicDeleteIncome(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("逻辑删除，数据id:{}", id);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", getUserId(request));
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = incomeDAO.delete(params);
        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public void deleteIncome(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = incomeDAO.pdelete(params);
        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public IncomeDTO findIncomeById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        IncomeDTO incomeDTO = incomeDAO.selectOneDTO(params);
        return incomeDTO;
    }

    @Override
    public IncomeDTO findOneIncome(@NonNull Map<String, Object> params) {
        log.info("find one params:{}", params);
        Income income = incomeDAO.selectOne(params);
        IncomeDTO incomeDTO = new IncomeDTO();
        if (null != income) {
            BeanUtils.copyProperties(income, incomeDTO);
        }
        return incomeDTO;
    }

    @Override
    public List<IncomeDTO> find(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params = getUnionParams(params, scs, pc);
        List<IncomeDTO> resultList = incomeDAO.selectDTO(params);
        return resultList;
    }

    @Override
    public List<Map> findMap(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc, String... columns) throws BizException {
        if (columns.length == 0) {
            throw new BizException("columns长度不能为0");
        }
        params = getUnionParams(params, scs, pc);
        params.put("columns", columns);
        return incomeDAO.selectMap(params);
    }

    private Map<String, Object> getUnionParams(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params.put("pc", pc);
        params.put("scs", scs);
        return params;
    }

    @Override
    public int count(@NonNull Map<String, Object> params) {
        return incomeDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = incomeDAO.groupCount(conditions);
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
        return incomeDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = incomeDAO.groupSum(conditions);
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
