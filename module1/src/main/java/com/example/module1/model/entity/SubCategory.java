package com.example.module1.model.entity;

import com.example.core.config.LongJsonSerializer;
import com.example.core.model.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

/**
 * <p>
 * 子分类表
 * </p>
 *
 * @package:  com.example.module1.entity
 * @description: 子分类表
 */
@Data
@ApiModel(description = "子分类表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubCategory extends BaseEntity implements Serializable {

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
  /**
   * 用户表id
   */
  @ApiModelProperty(value = "用户表id")
  @JsonSerialize(using = LongJsonSerializer.class)
  private Long userId;

}
