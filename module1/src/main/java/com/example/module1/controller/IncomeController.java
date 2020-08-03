package com.example.module1.controller;

import com.example.core.common.PagingContext;
import com.example.core.common.SortingContext;
import com.example.core.common.R;
import com.example.core.util.I18nUtils;
import com.example.module1.model.dto.IncomeDTO;
import com.example.module1.model.entity.Income;
import com.example.module1.service.IncomeService;
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
 * 收入明细表
 * </p>
 *
 * @package:  com.example.module1.controller
 * @description: 收入明细表
 * @version: V1.0
 */
@RestController
@RequestMapping("/income")
@Slf4j
@Api("收入明细表")
public class IncomeController extends BaseController<Income> {

    @Autowired
    private IncomeService incomeService;

    @ActionFlag(detail = "Income_list")
    @ApiOperation(value = "分页查询收入明细表", notes = "分页查询收入明细表以及排序功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "每页的条数", paramType = "query"),
            @ApiImplicitParam(name = "p", value = "请求的页码", paramType = "query"),
            @ApiImplicitParam(name = "scs", value = "排序字段，格式：scs=name(asc)&scs=age(desc)", paramType = "query")
    })
    @GetMapping(name = "查询-收入明细表列表")
    public Object list(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        int total = incomeService.count(params);
        PagingContext pc;
        Vector<SortingContext> scs;
        List<IncomeDTO> list = new ArrayList<>();
        pc = getPagingContext(request, total);
        if (total > 0) {
            scs = getSortingContext(request);
            list = incomeService.find(params, scs, pc);
        }
        return R.success(list, pc);
    }

    @ApiOperation(value = "通过id查询收入明细表", notes = "通过id查询收入明细表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    @GetMapping(value = "/{id}", name="详情")
    public Object view(@PathVariable("id") Long id) {
        log.info("get income Id:{}", id);
        return R.success(incomeService.findIncomeById(id));
    }

    @ApiOperation(value = "通过查询条件查询收入明细表一条数据", notes = "通过查询条件查询收入明细表一条数据")
    @GetMapping(value = "/findOne", name="通过查询条件查询收入明细表一条数据")
    public Object findOne(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        log.info("get income findOne params:{}", params);
        int total = incomeService.count(params);
        if (total > 1) {
            log.error("get income findOne params: {}, error message:{}", params, "查询失败，返回多条数据");
            return R.fail(ErrorCodeEnum.FAIL_CODE.getCode(), I18nUtils.get("query.failed.return.multiple.data", getLang(request)));
        }
        IncomeDTO incomeDTO = null;
        if (total == 1) {
            incomeDTO = incomeService.findOneIncome(params);
        }
        return R.success(incomeDTO);
    }

    @ActionFlag(detail = "Income_add")
    @PostMapping(name = "创建")
    @ApiOperation(value = "新增收入明细表", notes = "新增收入明细表")
    public Object create(@RequestBody IncomeDTO incomeDTO, HttpServletRequest request) {
        log.info("add income DTO:{}", incomeDTO);
        try {
            incomeService.saveIncome(incomeDTO, request);
        } catch (BizException e) {
            log.error("add income failed, incomeDTO: {}, error message:{}, error all:{}", incomeDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.INSERT_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "Income_update")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改收入明细表", notes = "修改收入明细表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object update(@PathVariable("id") Long id, @RequestBody IncomeDTO incomeDTO, HttpServletRequest request) {
        log.info("put modify id:{}, income DTO:{}", id, incomeDTO);
        try {
            incomeService.updateIncome(id, incomeDTO, request);
        } catch (BizException e) {
            log.error("update income failed, incomeDTO: {}, error message:{}, error all:{}", incomeDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.UPDATE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "Income_delete")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除收入明细表", notes = "删除收入明细表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object remove(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("delete income, id:{}", id);
        try {
            incomeService.logicDeleteIncome(id, request);
        } catch (BizException e) {
            log.error("delete failed, income id: {}, error message:{}, error all:{}", id, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.DELETE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

}
