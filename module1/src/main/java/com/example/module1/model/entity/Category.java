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
 * 主分类表
 * </p>
 *
 * @package:  com.example.module1.entity
 * @description: 主分类表
 * @author: fenmi
 * @date: Created in 2020-07-31 15:59:42
 * @copyright: Copyright (c) 2020
 */
@Data
@ApiModel(description = "主分类表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category extends BaseEntity implements Serializable {

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
