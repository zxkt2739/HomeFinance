package com.example.module1.service.impl;

import com.example.core.util.I18nUtils;
import com.example.module1.dao.ExpendDAO;
import com.example.module1.model.dto.ExpendDTO;
import com.example.module1.model.entity.Expend;
import com.example.module1.service.ExpendService;
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
 * 支出明细表
 * </p>
 *
 * @package: com.example.module1.service.impl
 * @description: 支出明细表
 * @version: V1.0
 */
@Service
@Slf4j
public class ExpendServiceImpl extends BaseServiceImpl implements ExpendService {

    @Autowired
    private ExpendDAO expendDAO;

    @Override
    public void saveExpend(@NonNull ExpendDTO expendDTO, HttpServletRequest request) throws BizException {
        Expend expend = BeanUtil.copyProperties(expendDTO, new Expend());
        log.info("save Expend:{}", expend);
        if (expendDAO.insert((Expend) this.packAddBaseProps(expend, request)) != 1) {
            log.error("insert error, data:{}", expend);
            throw new BizException("Insert expend Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExpendList(@NonNull List<Expend> expendList, HttpServletRequest request) throws BizException {
        if (expendList.size() == 0) {
            throw new BizException(I18nUtils.get("parameter.rule.length", getLang(request)));
        }
        int rows = expendDAO.insertList(expendList);
        if (rows != expendList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, expendList.size());
            throw new BizException(I18nUtils.get("batch.saving.exception", getLang(request)));
        }
    }

    @Override
    public void updateExpend(@NonNull Long id, @NonNull ExpendDTO expendDTO, HttpServletRequest request) throws BizException {
        log.info("full update expendDTO:{}", expendDTO);
        Expend expend = BeanUtil.copyProperties(expendDTO, new Expend());
        expend.setId(id);
        int cnt = expendDAO.update((Expend) this.packModifyBaseProps(expend, request));
        if (cnt != 1) {
            log.error("update error, data:{}", expendDTO);
            throw new BizException("update expend Error!");
        }
    }

    @Override
    public void updateExpendSelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        expendDAO.updatex(params);
    }

    @Override
    public void logicDeleteExpend(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("逻辑删除，数据id:{}", id);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", getUserId(request));
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = expendDAO.delete(params);
        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public void deleteExpend(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = expendDAO.pdelete(params);
        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public ExpendDTO findExpendById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        ExpendDTO expendDTO = expendDAO.selectOneDTO(params);
        return expendDTO;
    }

    @Override
    public ExpendDTO findOneExpend(@NonNull Map<String, Object> params) {
        log.info("find one params:{}", params);
        Expend expend = expendDAO.selectOne(params);
        ExpendDTO expendDTO = new ExpendDTO();
        if (null != expend) {
            BeanUtils.copyProperties(expend, expendDTO);
        }
        return expendDTO;
    }

    @Override
    public List<ExpendDTO> find(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params = getUnionParams(params, scs, pc);
        List<ExpendDTO> resultList = expendDAO.selectDTO(params);
        return resultList;
    }

    @Override
    public List<Map> findMap(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc, String... columns) throws BizException {
        if (columns.length == 0) {
            throw new BizException("columns长度不能为0");
        }
        params = getUnionParams(params, scs, pc);
        params.put("columns", columns);
        return expendDAO.selectMap(params);
    }

    private Map<String, Object> getUnionParams(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params.put("pc", pc);
        params.put("scs", scs);
        return params;
    }

    @Override
    public int count(@NonNull Map<String, Object> params) {
        return expendDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = expendDAO.groupCount(conditions);
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
        return expendDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = expendDAO.groupSum(conditions);
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
