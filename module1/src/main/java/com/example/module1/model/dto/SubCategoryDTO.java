package com.example.module1.model.dto;

import com.example.core.config.LongJsonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.core.model.dto.BaseDTO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
/**
 * <p>
 * 子分类表
 * </p>
 *
 * @description: 子分类表
 * @author: fenmi
 * @date: Created in 2020-07-31 15:59:45
 */
@ApiModel("子分类表")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubCategoryDTO extends BaseDTO implements Serializable {

    /**
     * 子类型名称
     */
    @ApiModelProperty(value = "子类型名称")
    private String name;
    /**
     * 主分类表id
     */
    @ApiModelProperty(value = "主分类表id")
    @JsonSerialize(using = LongJsonSerializer.class)
    private Long categoryId;
    /**
     * 类别：1-支出，2-收入
     */
    @ApiModelProperty(value = "类别：1-支出，2-收入")
    private Integer type;

}
