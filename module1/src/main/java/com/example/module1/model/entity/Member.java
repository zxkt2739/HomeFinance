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
 * 用户表
 * </p>
 *
 * @package:  com.example.module1.entity
 * @description: 用户表
 * @author: fenmi
 * @date: Created in 2020-08-03 10:38:53
 * @copyright: Copyright (c) 2020
 */
@Data
@ApiModel(description = "用户表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member extends BaseEntity implements Serializable {

  /**
   * 姓名
   */
  @ApiModelProperty(value = "姓名")
  private String name;
  /**
   * 用户表id
   */
  @ApiModelProperty(value = "用户表id")
  @JsonSerialize(using = LongJsonSerializer.class)
  private Long userId;

}
