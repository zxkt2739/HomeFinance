package com.example.module1.controller;

import com.example.core.common.PagingContext;
import com.example.core.common.SortingContext;
import com.example.core.common.R;
import com.example.core.util.I18nUtils;
import com.example.module1.model.dto.CategoryDTO;
import com.example.module1.model.entity.Category;
import com.example.module1.service.CategoryService;
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
 * 主分类表
 * </p>
 *
 * @package:  com.example.module1.controller
 * @description: 主分类表
 * @author: fenmi
 * @date: Created in 2020-07-31 16:00:13
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: fenmi
 */
@RestController
@RequestMapping("/category")
@Slf4j
@Api("主分类表")
public class CategoryController extends BaseController<Category> {

    @Autowired
    private CategoryService categoryService;

    @ActionFlag(detail = "Category_list")
    @ApiOperation(value = "分页查询主分类表", notes = "分页查询主分类表以及排序功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "每页的条数", paramType = "query"),
            @ApiImplicitParam(name = "p", value = "请求的页码", paramType = "query"),
            @ApiImplicitParam(name = "scs", value = "排序字段，格式：scs=name(asc)&scs=age(desc)", paramType = "query")
    })
    @GetMapping(name = "查询-主分类表列表")
    public Object list(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        int total = categoryService.count(params);
        PagingContext pc;
        Vector<SortingContext> scs;
        List<CategoryDTO> list = new ArrayList<>();
        pc = getPagingContext(request, total);
        if (total > 0) {
            scs = getSortingContext(request);
            list = categoryService.find(params, scs, pc);
        }
        return R.success(list, pc);
    }

    @ApiOperation(value = "通过id查询主分类表", notes = "通过id查询主分类表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    @GetMapping(value = "/{id}", name="详情")
    public Object view(@PathVariable("id") Long id) {
        log.info("get category Id:{}", id);
        return R.success(categoryService.findCategoryById(id));
    }

    @ApiOperation(value = "通过查询条件查询主分类表一条数据", notes = "通过查询条件查询主分类表一条数据")
    @GetMapping(value = "/findOne", name="通过查询条件查询主分类表一条数据")
    public Object findOne(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        log.info("get category findOne params:{}", params);
        int total = categoryService.count(params);
        if (total > 1) {
            log.error("get category findOne params: {}, error message:{}", params, "查询失败，返回多条数据");
            return R.fail(ErrorCodeEnum.FAIL_CODE.getCode(), I18nUtils.get("query.failed.return.multiple.data", getLang(request)));
        }
        CategoryDTO categoryDTO = null;
        if (total == 1) {
            categoryDTO = categoryService.findOneCategory(params);
        }
        return R.success(categoryDTO);
    }

    @ActionFlag(detail = "Category_add")
    @PostMapping(name = "创建")
    @ApiOperation(value = "新增主分类表", notes = "新增主分类表")
    public Object create(@RequestBody CategoryDTO categoryDTO, HttpServletRequest request) {
        log.info("add category DTO:{}", categoryDTO);
        try {
            categoryService.saveCategory(categoryDTO, request);
        } catch (BizException e) {
            log.error("add category failed, categoryDTO: {}, error message:{}, error all:{}", categoryDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.INSERT_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "Category_update")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改主分类表", notes = "修改主分类表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object update(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO, HttpServletRequest request) {
        log.info("put modify id:{}, category DTO:{}", id, categoryDTO);
        try {
            categoryService.updateCategory(id, categoryDTO, request);
        } catch (BizException e) {
            log.error("update category failed, categoryDTO: {}, error message:{}, error all:{}", categoryDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.UPDATE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "Category_delete")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除主分类表", notes = "删除主分类表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object remove(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("delete category, id:{}", id);
        try {
            categoryService.logicDeleteCategory(id, request);
        } catch (BizException e) {
            log.error("delete failed, category id: {}, error message:{}, error all:{}", id, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.DELETE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

}
