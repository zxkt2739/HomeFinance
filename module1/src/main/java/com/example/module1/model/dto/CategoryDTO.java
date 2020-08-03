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
 * 主分类表
 * </p>
 *
 * @description: 主分类表
 */
@ApiModel("主分类表")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO extends BaseDTO implements Serializable {

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String name;
    /**
     * 类别：1-支出，2-收入
     */
    @ApiModelProperty(value = "类别：1-支出，2-收入")
    private Integer type;
    /**
     * 用户表id
     */
    @ApiModelProperty(value = "用户表id")
    @JsonSerialize(using = LongJsonSerializer.class)
    private Long userId;

}
