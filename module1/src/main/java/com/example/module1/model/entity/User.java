package com.example.module1.model.entity;

import com.example.core.model.entity.BaseEntity;
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
 */
@Data
@ApiModel(description = "用户表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseEntity implements Serializable {

  /**
   * 姓名
   */
  @ApiModelProperty(value = "姓名")
  private String name;
  /**
   * 用户名
   */
  @ApiModelProperty(value = "用户名")
  private String userName;

}
