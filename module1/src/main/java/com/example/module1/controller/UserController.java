package com.example.module1.controller;

import com.example.core.common.PagingContext;
import com.example.core.common.SortingContext;
import com.example.core.common.R;
import com.example.core.util.I18nUtils;
import com.example.module1.model.dto.UserDTO;
import com.example.module1.model.entity.User;
import com.example.module1.service.UserService;
import com.example.module1.filter.ActionFlag;
import com.example.core.enumeration.ErrorCodeEnum;
import com.example.core.exception.BizException;
import com.example.core.controller.BaseController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 用户表
 * </p>
 *
 * @package:  com.example.module1.controller
 * @description: 用户表
 * @version: V1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api("用户表")
public class UserController extends BaseController<User> {

    @Autowired
    private UserService userService;

    @ActionFlag(detail = "User_list")
    @ApiOperation(value = "分页查询用户表", notes = "分页查询用户表以及排序功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "每页的条数", paramType = "query"),
            @ApiImplicitParam(name = "p", value = "请求的页码", paramType = "query"),
            @ApiImplicitParam(name = "scs", value = "排序字段，格式：scs=name(asc)&scs=age(desc)", paramType = "query")
    })
    @GetMapping(name = "查询-用户表列表")
    public Object list(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        int total = userService.count(params);
        PagingContext pc;
        Vector<SortingContext> scs;
        List<UserDTO> list = new ArrayList<>();
        pc = getPagingContext(request, total);
        if (total > 0) {
            scs = getSortingContext(request);
            list = userService.find(params, scs, pc);
        }
        return R.success(list, pc);
    }

    @ApiOperation(value = "通过id查询用户表", notes = "通过id查询用户表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    @GetMapping(value = "/{id}", name="详情")
    public Object view(@PathVariable("id") Long id) {
        log.info("get user Id:{}", id);
        return R.success(userService.findUserById(id));
    }

    @ApiOperation(value = "通过查询条件查询用户表一条数据", notes = "通过查询条件查询用户表一条数据")
    @GetMapping(value = "/findOne", name="通过查询条件查询用户表一条数据")
    public Object findOne(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        log.info("get user findOne params:{}", params);
        int total = userService.count(params);
        if (total > 1) {
            log.error("get user findOne params: {}, error message:{}", params, "查询失败，返回多条数据");
            return R.fail(ErrorCodeEnum.FAIL_CODE.getCode(), I18nUtils.get("query.failed.return.multiple.data", getLang(request)));
        }
        UserDTO userDTO = null;
        if (total == 1) {
            userDTO = userService.findOneUser(params);
        }
        return R.success(userDTO);
    }

    @ActionFlag(detail = "User_add")
    @PostMapping(name = "创建")
    @ApiOperation(value = "新增用户表", notes = "新增用户表")
    public Object create(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        log.info("add user DTO:{}", userDTO);
        try {
            userService.saveUser(userDTO, request);
        } catch (BizException e) {
            log.error("add user failed, userDTO: {}, error message:{}, error all:{}", userDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.INSERT_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "User_update")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改用户表", notes = "修改用户表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object update(@PathVariable("id") Long id, @RequestBody UserDTO userDTO, HttpServletRequest request) {
        log.info("put modify id:{}, user DTO:{}", id, userDTO);
        try {
            userService.updateUser(id, userDTO, request);
        } catch (BizException e) {
            log.error("update user failed, userDTO: {}, error message:{}, error all:{}", userDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.UPDATE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "User_delete")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户表", notes = "删除用户表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object remove(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("delete user, id:{}", id);
        try {
            userService.logicDeleteUser(id, request);
        } catch (BizException e) {
            log.error("delete failed, user id: {}, error message:{}, error all:{}", id, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.DELETE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

}
