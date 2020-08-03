package com.example.module1.controller;

import com.example.core.common.PagingContext;
import com.example.core.common.SortingContext;
import com.example.core.common.R;
import com.example.core.util.I18nUtils;
import com.example.module1.model.dto.ExpendDTO;
import com.example.module1.model.entity.Expend;
import com.example.module1.service.ExpendService;
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
 * 支出明细表
 * </p>
 *
 * @package:  com.example.module1.controller
 * @description: 支出明细表
 * @version: V1.0
 */
@RestController
@RequestMapping("/expend")
@Slf4j
@Api("支出明细表")
public class ExpendController extends BaseController<Expend> {

    @Autowired
    private ExpendService expendService;

    @ActionFlag(detail = "Expend_list")
    @ApiOperation(value = "分页查询支出明细表", notes = "分页查询支出明细表以及排序功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "每页的条数", paramType = "query"),
            @ApiImplicitParam(name = "p", value = "请求的页码", paramType = "query"),
            @ApiImplicitParam(name = "scs", value = "排序字段，格式：scs=name(asc)&scs=age(desc)", paramType = "query")
    })
    @GetMapping(name = "查询-支出明细表列表")
    public Object list(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        int total = expendService.count(params);
        PagingContext pc;
        Vector<SortingContext> scs;
        List<ExpendDTO> list = new ArrayList<>();
        pc = getPagingContext(request, total);
        if (total > 0) {
            scs = getSortingContext(request);
            list = expendService.find(params, scs, pc);
        }
        return R.success(list, pc);
    }

    @ApiOperation(value = "通过id查询支出明细表", notes = "通过id查询支出明细表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    @GetMapping(value = "/{id}", name="详情")
    public Object view(@PathVariable("id") Long id) {
        log.info("get expend Id:{}", id);
        return R.success(expendService.findExpendById(id));
    }

    @ApiOperation(value = "通过查询条件查询支出明细表一条数据", notes = "通过查询条件查询支出明细表一条数据")
    @GetMapping(value = "/findOne", name="通过查询条件查询支出明细表一条数据")
    public Object findOne(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        log.info("get expend findOne params:{}", params);
        int total = expendService.count(params);
        if (total > 1) {
            log.error("get expend findOne params: {}, error message:{}", params, "查询失败，返回多条数据");
            return R.fail(ErrorCodeEnum.FAIL_CODE.getCode(), I18nUtils.get("query.failed.return.multiple.data", getLang(request)));
        }
        ExpendDTO expendDTO = null;
        if (total == 1) {
            expendDTO = expendService.findOneExpend(params);
        }
        return R.success(expendDTO);
    }

    @ActionFlag(detail = "Expend_add")
    @PostMapping(name = "创建")
    @ApiOperation(value = "新增支出明细表", notes = "新增支出明细表")
    public Object create(@RequestBody ExpendDTO expendDTO, HttpServletRequest request) {
        log.info("add expend DTO:{}", expendDTO);
        try {
            expendService.saveExpend(expendDTO, request);
        } catch (BizException e) {
            log.error("add expend failed, expendDTO: {}, error message:{}, error all:{}", expendDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.INSERT_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "Expend_update")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改支出明细表", notes = "修改支出明细表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object update(@PathVariable("id") Long id, @RequestBody ExpendDTO expendDTO, HttpServletRequest request) {
        log.info("put modify id:{}, expend DTO:{}", id, expendDTO);
        try {
            expendService.updateExpend(id, expendDTO, request);
        } catch (BizException e) {
            log.error("update expend failed, expendDTO: {}, error message:{}, error all:{}", expendDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.UPDATE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "Expend_delete")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除支出明细表", notes = "删除支出明细表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object remove(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("delete expend, id:{}", id);
        try {
            expendService.logicDeleteExpend(id, request);
        } catch (BizException e) {
            log.error("delete failed, expend id: {}, error message:{}, error all:{}", id, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.DELETE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

}
