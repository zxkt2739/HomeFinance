package com.example.module1.controller;

import com.example.core.common.PagingContext;
import com.example.core.common.SortingContext;
import com.example.core.common.R;
import com.example.core.util.I18nUtils;
import com.example.module1.model.dto.SubCategoryDTO;
import com.example.module1.model.entity.SubCategory;
import com.example.module1.service.SubCategoryService;
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
 * 子分类表
 * </p>
 *
 * @package:  com.example.module1.controller
 * @description: 子分类表
 * @version: V1.0
 */
@RestController
@RequestMapping("/subCategory")
@Slf4j
@Api("子分类表")
public class SubCategoryController extends BaseController<SubCategory> {

    @Autowired
    private SubCategoryService subCategoryService;

    @ActionFlag(detail = "SubCategory_list")
    @ApiOperation(value = "分页查询子分类表", notes = "分页查询子分类表以及排序功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "每页的条数", paramType = "query"),
            @ApiImplicitParam(name = "p", value = "请求的页码", paramType = "query"),
            @ApiImplicitParam(name = "scs", value = "排序字段，格式：scs=name(asc)&scs=age(desc)", paramType = "query")
    })
    @GetMapping(name = "查询-子分类表列表")
    public Object list(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        int total = subCategoryService.count(params);
        PagingContext pc;
        Vector<SortingContext> scs;
        List<SubCategoryDTO> list = new ArrayList<>();
        pc = getPagingContext(request, total);
        if (total > 0) {
            scs = getSortingContext(request);
            list = subCategoryService.find(params, scs, pc);
        }
        return R.success(list, pc);
    }

    @ApiOperation(value = "通过id查询子分类表", notes = "通过id查询子分类表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    @GetMapping(value = "/{id}", name="详情")
    public Object view(@PathVariable("id") Long id) {
        log.info("get subCategory Id:{}", id);
        return R.success(subCategoryService.findSubCategoryById(id));
    }

    @ApiOperation(value = "通过查询条件查询子分类表一条数据", notes = "通过查询条件查询子分类表一条数据")
    @GetMapping(value = "/findOne", name="通过查询条件查询子分类表一条数据")
    public Object findOne(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        log.info("get subCategory findOne params:{}", params);
        int total = subCategoryService.count(params);
        if (total > 1) {
            log.error("get subCategory findOne params: {}, error message:{}", params, "查询失败，返回多条数据");
            return R.fail(ErrorCodeEnum.FAIL_CODE.getCode(), I18nUtils.get("query.failed.return.multiple.data", getLang(request)));
        }
        SubCategoryDTO subCategoryDTO = null;
        if (total == 1) {
            subCategoryDTO = subCategoryService.findOneSubCategory(params);
        }
        return R.success(subCategoryDTO);
    }

    @ActionFlag(detail = "SubCategory_add")
    @PostMapping(name = "创建")
    @ApiOperation(value = "新增子分类表", notes = "新增子分类表")
    public Object create(@RequestBody SubCategoryDTO subCategoryDTO, HttpServletRequest request) {
        log.info("add subCategory DTO:{}", subCategoryDTO);
        try {
            subCategoryService.saveSubCategory(subCategoryDTO, request);
        } catch (BizException e) {
            log.error("add subCategory failed, subCategoryDTO: {}, error message:{}, error all:{}", subCategoryDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.INSERT_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "SubCategory_update")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改子分类表", notes = "修改子分类表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object update(@PathVariable("id") Long id, @RequestBody SubCategoryDTO subCategoryDTO, HttpServletRequest request) {
        log.info("put modify id:{}, subCategory DTO:{}", id, subCategoryDTO);
        try {
            subCategoryService.updateSubCategory(id, subCategoryDTO, request);
        } catch (BizException e) {
            log.error("update subCategory failed, subCategoryDTO: {}, error message:{}, error all:{}", subCategoryDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.UPDATE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "SubCategory_delete")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除子分类表", notes = "删除子分类表")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object remove(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("delete subCategory, id:{}", id);
        try {
            subCategoryService.logicDeleteSubCategory(id, request);
        } catch (BizException e) {
            log.error("delete failed, subCategory id: {}, error message:{}, error all:{}", id, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.DELETE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

}
