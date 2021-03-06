package com.example.module1.model.entity;

import com.example.core.config.LongDateSerializer;
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
  /**
   * 密码sha1
   */
  @ApiModelProperty(value = "密码sha1")
  private String password;
  /**
   * 干扰码
   */
  @ApiModelProperty(value = "干扰码")
  private Integer salt;
  /**
   * 本次登录时间
   */
  @JsonSerialize(using = LongDateSerializer.class)
  @ApiModelProperty(value = "本次登录时间")
  private Long loginTime;
  /**
   * 上次登录时间
   */
  @JsonSerialize(using = LongDateSerializer.class)
  @ApiModelProperty(value = "最后登录时间")
  private Long lastLoginTime;
  /**
   * 本次登录ip
   */
  @ApiModelProperty(value = "本次登录ip")
  private String loginIp;
  /**
   * 上次登录IP
   */
  @ApiModelProperty(value = "最后登录IP")
  private String lastLoginIp;
  /**
   * 登录次数
   */
  @ApiModelProperty(value = "登录次数")
  private Integer loginTimes;
  /**
   * 密码错误次数
   */
  @ApiModelProperty(value = "密码错误次数")
  private Integer errorTimes;

}
