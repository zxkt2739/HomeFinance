package com.example.module1.controller;

import com.example.core.common.R;
import com.example.core.enumeration.ErrorCodeEnum;
import com.example.core.exception.BizException;
import com.example.core.util.JwtUtils;
import com.example.module1.filter.PassToken;
import com.example.module1.model.dto.UserDTO;
import com.example.module1.service.LoginService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;

    /**
     * 登陆页面
     *
     * @return
     */
    @PassToken
    @GetMapping(value = "/login", name = "用户登陆")
    @ApiOperation(value = "用户登陆", notes = "用户登陆")
    public Object login(UserDTO userDTO, HttpServletRequest request) {
        //1.执行登陆方法
        String token;
        try {
            token = loginService.jwtLogin(userDTO.getUserName(), DigestUtils.md5Hex(userDTO.getPassword()), request);
            Map<String, Object> params = new HashMap<>(1);
            params.put("token", token);
            return R.success(params);
        } catch (BizException e) {
            //登陆失败或第一次登陆没有token或token失效或没有权限访问该页面或token验证码生成失败
            return R.fail(ErrorCodeEnum.LOGIN__ERROR.getCode(),e.getMessage());
        }
    }

    /**
     * 登陆成功,返回需要的基本用户信息
     *
     * @return
     */
    @PostMapping(value = "/user", name = "登陆成功,返回需要的基本用户信息")
    @ApiOperation(value = "登陆成功,返回需要的基本用户信息", notes = "登陆成功,返回需要的基本用户信息")
    public Object user(HttpServletRequest request) {
        String headTokenValue = request.getHeader("Authorization");
        String loginName = JwtUtils.getUsername(headTokenValue.replace("Bearer ", ""));
        UserDTO admin = loginService.findAdminPartOfDataByUsername(loginName);
        List<String> menu = loginService.findMenuAction(loginName);
        List<String> actions = loginService.findAction(loginName);
        Map<String, Object> params = new HashMap<>(3);
        params.put("admin", admin);
        params.put("menu", menu);
        params.put("actions", actions);
        return R.success(params);
    }
}
