package com.example.module1.service.impl;

import com.example.core.util.I18nUtils;
import com.example.module1.dao.MemberDAO;
import com.example.module1.model.dto.MemberDTO;
import com.example.module1.model.entity.Member;
import com.example.module1.service.MemberService;
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
 * 用户表
 * </p>
 *
 * @package: com.example.module1.service.impl
 * @description: 用户表
 * @version: V1.0
 */
@Service
@Slf4j
public class MemberServiceImpl extends BaseServiceImpl implements MemberService {

    @Autowired
    private MemberDAO memberDAO;

    @Override
    public void saveMember(@NonNull MemberDTO memberDTO, HttpServletRequest request) throws BizException {
        Member member = BeanUtil.copyProperties(memberDTO, new Member());
        log.info("save Member:{}", member);
        if (memberDAO.insert((Member) this.packAddBaseProps(member, request)) != 1) {
            log.error("insert error, data:{}", member);
            throw new BizException("Insert member Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMemberList(@NonNull List<Member> memberList, HttpServletRequest request) throws BizException {
        if (memberList.size() == 0) {
            throw new BizException(I18nUtils.get("parameter.rule.length", getLang(request)));
        }
        int rows = memberDAO.insertList(memberList);
        if (rows != memberList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, memberList.size());
            throw new BizException(I18nUtils.get("batch.saving.exception", getLang(request)));
        }
    }

    @Override
    public void updateMember(@NonNull Long id, @NonNull MemberDTO memberDTO, HttpServletRequest request) throws BizException {
        log.info("full update memberDTO:{}", memberDTO);
        Member member = BeanUtil.copyProperties(memberDTO, new Member());
        member.setId(id);
        int cnt = memberDAO.update((Member) this.packModifyBaseProps(member, request));
        if (cnt != 1) {
            log.error("update error, data:{}", memberDTO);
            throw new BizException("update member Error!");
        }
    }

    @Override
    public void updateMemberSelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        memberDAO.updatex(params);
    }

    @Override
    public void logicDeleteMember(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("逻辑删除，数据id:{}", id);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", getUserId(request));
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = memberDAO.delete(params);
        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public void deleteMember(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = memberDAO.pdelete(params);
        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public MemberDTO findMemberById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        MemberDTO memberDTO = memberDAO.selectOneDTO(params);
        return memberDTO;
    }

    @Override
    public MemberDTO findOneMember(@NonNull Map<String, Object> params) {
        log.info("find one params:{}", params);
        Member member = memberDAO.selectOne(params);
        MemberDTO memberDTO = new MemberDTO();
        if (null != member) {
            BeanUtils.copyProperties(member, memberDTO);
        }
        return memberDTO;
    }

    @Override
    public List<MemberDTO> find(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params = getUnionParams(params, scs, pc);
        List<MemberDTO> resultList = memberDAO.selectDTO(params);
        return resultList;
    }

    @Override
    public List<Map> findMap(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc, String... columns) throws BizException {
        if (columns.length == 0) {
            throw new BizException("columns长度不能为0");
        }
        params = getUnionParams(params, scs, pc);
        params.put("columns", columns);
        return memberDAO.selectMap(params);
    }

    private Map<String, Object> getUnionParams(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params.put("pc", pc);
        params.put("scs", scs);
        return params;
    }

    @Override
    public int count(@NonNull Map<String, Object> params) {
        return memberDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = memberDAO.groupCount(conditions);
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
        return memberDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = memberDAO.groupSum(conditions);
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
