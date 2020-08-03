package com.example.module1.service.impl;

import com.example.core.util.I18nUtils;
import com.example.module1.dao.UserDAO;
import com.example.module1.model.dto.UserDTO;
import com.example.module1.model.entity.User;
import com.example.module1.service.UserService;
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
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void saveUser(@NonNull UserDTO userDTO, HttpServletRequest request) throws BizException {
        User user = BeanUtil.copyProperties(userDTO, new User());
        log.info("save User:{}", user);
        if (userDAO.insert((User) this.packAddBaseProps(user, request)) != 1) {
            log.error("insert error, data:{}", user);
            throw new BizException("Insert user Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserList(@NonNull List<User> userList, HttpServletRequest request) throws BizException {
        if (userList.size() == 0) {
            throw new BizException(I18nUtils.get("parameter.rule.length", getLang(request)));
        }
        int rows = userDAO.insertList(userList);
        if (rows != userList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, userList.size());
            throw new BizException(I18nUtils.get("batch.saving.exception", getLang(request)));
        }
    }

    @Override
    public void updateUser(@NonNull Long id, @NonNull UserDTO userDTO, HttpServletRequest request) throws BizException {
        log.info("full update userDTO:{}", userDTO);
        User user = BeanUtil.copyProperties(userDTO, new User());
        user.setId(id);
        int cnt = userDAO.update((User) this.packModifyBaseProps(user, request));
        if (cnt != 1) {
            log.error("update error, data:{}", userDTO);
            throw new BizException("update user Error!");
        }
    }

    @Override
    public void updateUserSelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        userDAO.updatex(params);
    }

    @Override
    public void logicDeleteUser(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("逻辑删除，数据id:{}", id);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", getUserId(request));
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = userDAO.delete(params);
        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public void deleteUser(@NonNull Long id, HttpServletRequest request) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = userDAO.pdelete(params);
        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException(I18nUtils.get("delete.failed", getLang(request)));
        }
    }

    @Override
    public UserDTO findUserById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        UserDTO userDTO = userDAO.selectOneDTO(params);
        return userDTO;
    }

    @Override
    public UserDTO findOneUser(@NonNull Map<String, Object> params) {
        log.info("find one params:{}", params);
        User user = userDAO.selectOne(params);
        UserDTO userDTO = new UserDTO();
        if (null != user) {
            BeanUtils.copyProperties(user, userDTO);
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> find(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params = getUnionParams(params, scs, pc);
        List<UserDTO> resultList = userDAO.selectDTO(params);
        return resultList;
    }

    @Override
    public List<Map> findMap(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc, String... columns) throws BizException {
        if (columns.length == 0) {
            throw new BizException("columns长度不能为0");
        }
        params = getUnionParams(params, scs, pc);
        params.put("columns", columns);
        return userDAO.selectMap(params);
    }

    private Map<String, Object> getUnionParams(@NonNull Map<String, Object> params, Vector<SortingContext> scs, PagingContext pc) {
        params.put("pc", pc);
        params.put("scs", scs);
        return params;
    }

    @Override
    public int count(@NonNull Map<String, Object> params) {
        return userDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = userDAO.groupCount(conditions);
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
        return userDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = userDAO.groupSum(conditions);
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
