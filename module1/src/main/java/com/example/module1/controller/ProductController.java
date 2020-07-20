package com.example.module1.controller;

import com.example.core.common.PagingContext;
import com.example.core.common.SortingContext;
import com.example.core.common.R;
import com.example.core.util.I18nUtils;
import com.example.core.util.RedisUtils;
import com.example.module1.annotation.AccessLimit;
import com.example.module1.model.dto.ProductDTO;
import com.example.module1.model.entity.Product;
import com.example.module1.service.ProductService;
import com.example.module1.annotation.ActionFlag;
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
 * 
 * </p>
 *
 *
 */
@RestController
@RequestMapping("/product")
@Slf4j
@Api("")
public class ProductController extends BaseController<Product> {

    @Autowired
    private ProductService productService;

    @ActionFlag(detail = "Product_list")
    @ApiOperation(value = "分页查询", notes = "分页查询以及排序功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "每页的条数", paramType = "query"),
            @ApiImplicitParam(name = "p", value = "请求的页码", paramType = "query"),
            @ApiImplicitParam(name = "scs", value = "排序字段，格式：scs=name(asc)&scs=age(desc)", paramType = "query")
    })
    @GetMapping(name = "查询-列表")
    @AccessLimit(seconds = 5, maxCount = 3, needLogin = true)
    public Object list(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        int total = productService.count(params);
        PagingContext pc;
        Vector<SortingContext> scs;
        List<ProductDTO> list = new ArrayList<>();
        pc = getPagingContext(request, total);
        if (total > 0) {
            scs = getSortingContext(request);
            list = productService.find(params, scs, pc);
        }
        return R.success(list, pc);
    }

    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    @GetMapping(value = "/{id}", name="详情")
    public Object view(@PathVariable("id") Long id) {
        log.info("get product Id:{}", id);
        return R.success(productService.findProductById(id));
    }

    @ApiOperation(value = "通过查询条件查询一条数据", notes = "通过查询条件查询一条数据")
    @GetMapping(value = "/findOne", name="通过查询条件查询一条数据")
    public Object findOne(HttpServletRequest request) {
        Map<String, Object> params = getConditionsMap(request);
        log.info("get product findOne params:{}", params);
        int total = productService.count(params);
        if (total > 1) {
            log.error("get product findOne params: {}, error message:{}", params, "查询失败，返回多条数据");
            return R.fail(ErrorCodeEnum.FAIL_CODE.getCode(), I18nUtils.get("query.failed.return.multiple.data", getLang(request)));
        }
        ProductDTO productDTO = null;
        if (total == 1) {
            productDTO = productService.findOneProduct(params);
        }
        return R.success(productDTO);
    }

    @ActionFlag(detail = "Product_add")
    @PostMapping(name = "创建")
    @ApiOperation(value = "新增", notes = "新增")
    public Object create(@RequestBody ProductDTO productDTO, HttpServletRequest request) {
        log.info("add product DTO:{}", productDTO);
        try {
            productService.saveProduct(productDTO, request);
        } catch (BizException e) {
            log.error("add product failed, productDTO: {}, error message:{}, error all:{}", productDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.INSERT_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "Product_update")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object update(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO, HttpServletRequest request) {
        log.info("put modify id:{}, product DTO:{}", id, productDTO);
        try {
            productService.updateProduct(id, productDTO, request);
        } catch (BizException e) {
            log.error("update product failed, productDTO: {}, error message:{}, error all:{}", productDTO, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.UPDATE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

    @ActionFlag(detail = "Product_delete")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true)
    public Object remove(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("delete product, id:{}", id);
        try {
            productService.logicDeleteProduct(id, request);
        } catch (BizException e) {
            log.error("delete failed, product id: {}, error message:{}, error all:{}", id, e.getMessage(), e);
            return R.fail(ErrorCodeEnum.DELETE_FAILED_ERROR.getCode(), e.getMessage());
        }
        return R.success();
    }

}
