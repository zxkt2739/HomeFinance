package com.example.module1.service;

import com.example.core.exception.BizException;
import com.example.module1.model.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LoginService {

    /**
     * 登陆
     * @param userName 账号
     * @param password 密码
     * @param request
     * @return
     * @throws BizException
     */
    String jwtLogin(String userName, String password, HttpServletRequest request) throws BizException;

    UserDTO findAdminPartOfDataByUsername(String loginName);

    List<String> findMenuAction(String loginName);

    List<String> findAction(String loginName);
}
