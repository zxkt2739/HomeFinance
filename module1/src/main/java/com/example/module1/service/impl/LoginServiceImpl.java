package com.example.module1.service.impl;

import com.example.core.common.Constant;
import com.example.core.exception.BizException;
import com.example.core.service.impl.BaseServiceImpl;
import com.example.module1.dao.UserDAO;
import com.example.module1.model.dto.UserDTO;
import com.example.module1.model.entity.User;
import com.example.module1.service.LoginService;
import com.example.module1.utils.JwtUtils;
import com.example.module1.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LoginServiceImpl extends BaseServiceImpl<User> implements LoginService  {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    UserDAO userDAO;

    @Override
    public String jwtLogin(String userName, String password, HttpServletRequest request) throws BizException {
        // 图片二维码校验
        String uuid = request.getParameter("uuid");
        String captchaCode = request.getParameter("captchaCode");
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(captchaCode)) {
            throw new BizException("用户名不能为空");
        }
        if (redisUtils.hasKey(uuid)) {
            // 获取redis里的保存的验证码
            String captchaCodeInRedis = (String) redisUtils.get(uuid);
            if (captchaCodeInRedis.equalsIgnoreCase(captchaCode)) {
                if (StringUtils.isEmpty(userName)) {
                    throw new BizException("用户名不能为空");
                }
                if (StringUtils.isEmpty(password)) {
                    // 密码不能为空
                    throw new BizException("密码不能为空");
                }
                // 设置管理员标识 admin ,用于在过滤器进行判断时知道用户身份.
                String sign = Constant.ADMIN;
                User user = null;
                Map<String, Object> params = new HashMap<>(1);
                params.put("userName", userName);
                user = userDAO.selectOne(params);
                if (user != null) {
                    if (redisUtils.hasKey(Constant.LOGIN_LOCK + user.getUserName() + user.getId())) {
                        // 允许登录的时间
                        long allowLoginTimeStamp = (long) redisUtils.get(Constant.LOGIN_LOCK + user.getUserName() + user.getId());
                        // 当前时间
                        long now = System.currentTimeMillis();
                        long seconds = (allowLoginTimeStamp - now) / 1000;
                        long minutes = seconds / 60;
                        if (seconds % 60 > 0) {
                            minutes++;
                        }
                        throw new BizException("连续3次用户名与密码不匹配, 请于" + minutes + "分钟后登录");
                    }
                    // 密码校验
                    if (!password.equals(user.getPassword())) {
                        Integer loginErrCnt = user.getErrorTimes();
                        if (loginErrCnt < 2) {
                            user.setErrorTimes(++loginErrCnt);
                            userDAO.update(user);
                            throw new BizException("密码错误,您还有" + (3 - loginErrCnt) + "次登录机会");
                        } else {
                            // 连续5次手机号或用户名与密码不匹配时， 第六次在点击【登录】按钮时， 弹出提示:
                            // 您已连续五次输入密码错误,请于60分钟后登录
                            // 登录次数置为0
                            user.setErrorTimes(0);
                            userDAO.update(user);
                            // redis里缓存账号锁定时间
                            long loginAllowTimeStamp = System.currentTimeMillis() + 3600 * 1000;
                            redisUtils.set(Constant.LOGIN_LOCK + user.getUserName() + user.getId(), loginAllowTimeStamp, 3600);
                            throw new BizException("连续3次用户名与密码不匹配, 您的账户已锁定，系统将于一小时后解锁");
                        }
                    }
                    // 登陆成功
                    // (1)先取出字段login_date本次登录时间,然后赋值给字段last_login_date上次登录时间
                    // (2)先取出字段login_ip本次登录IP,然后赋值给字段last_login_ip上次登录IP
                    userDAO.updateLastLoginDate(user.getId());
                    // 登录次数+1
                    int loginCnt = user.getLoginTimes();
                    User mer = new User();
                    mer.setLoginTimes(++loginCnt);
                    // (3)然后获取当前时间,存入字段login_date本次登录时间
                    // (4)然后获取当前登陆IP,存入字段login_ip本次登录IP
                    String clientIp = getIp(request);
                    long now = System.currentTimeMillis();
                    mer.setLoginIp(clientIp);
                    mer.setLoginTime(now);
                    mer.setId(user.getId());
                    // 重置错误登陆次数为0
                    mer.setErrorTimes(0);
                    userDAO.update(mer);
                    return JwtUtils.signs(userName, user.getId(), sign, password, redisUtils);
                } else {
                    throw new BizException("用户名或密码错误");
                }
            } else {
                throw new BizException("验证码错误");
            }
        } else {
            throw new BizException("验证码已过期");
        }
    }

    @Override
    public UserDTO findAdminPartOfDataByUsername(String userName) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("userName", userName);
        UserDTO userDTO = userDAO.selectOneDTO(params);
        List<String> roleNames = new ArrayList<>();
        roleNames.add("SuperAdmin");
        List<String> roleIds = new ArrayList<>();
        roleIds.add("1");
        userDTO.setRoleNames(roleNames);
        userDTO.setRoleIds(roleIds);
        /*List<AdminDTO> adminDTOS = adminDAO.selectRoleName(params);
        if (adminDTOS != null) {
            for (AdminDTO admin : adminDTOS) {
                roleNames.add(admin.getRoleName());
                roleId.add(admin.getRoleId());
            }
            adminDTO.setRoleNames(roleNames);
            adminDTO.setRoleIds(roleId);
        }*/
        return userDTO;
    }

    @Override
    public List<String> findMenuAction(String loginName) {
        List<String> menus = new ArrayList<>();
        menus.add("action_list");
        return menus;
    }

    @Override
    public List<String> findAction(String loginName) {
        List<String> actions = new ArrayList<>();
        actions.add("add");
        return actions;
    }
}
