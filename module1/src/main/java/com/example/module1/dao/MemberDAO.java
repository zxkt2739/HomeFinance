package com.example.module1.dao;

import com.example.module1.model.dto.MemberDTO;
import com.example.module1.model.entity.Member;
import com.example.core.dao.BaseDAO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @package:  com.example.module1.mapper
 * @description: 用户表
 * @version: V1.0
 */
@Mapper
public interface MemberDAO extends BaseDAO<Member> {

    /**
     * 根据查询条件得到指定字段集合的数据列表，包含分页和排序信息
     * @param params
     * @return
     */
    List<MemberDTO> selectDTO(Map<String, Object> params);

    /**
     * 根据id查询一条 MemberDTO
     * @param params
     * @return
     */
    MemberDTO selectOneDTO(Map<String, Object> params);

}
