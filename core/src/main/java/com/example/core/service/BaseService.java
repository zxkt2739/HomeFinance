package com.example.core.service;



import com.example.core.model.entity.BaseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author faker
 */
public interface BaseService<T extends BaseEntity> {

    T packAddBaseProps(T base, HttpServletRequest request);

    T packModifyBaseProps(T base, HttpServletRequest request);

    Long getUserId(HttpServletRequest request) ;

    String getIp(HttpServletRequest request);
}
