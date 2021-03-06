package com.example.module1.model.entity;

import com.example.core.config.LongDateSerializer;
import com.example.core.config.LongJsonSerializer;
import com.example.core.model.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 支出明细表
 * </p>
 *
 * @package:  com.example.module1.entity
 * @description: 支出明细表
 */
@Data
@ApiModel(description = "支出明细表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Expend extends BaseEntity implements Serializable {

  /**
   * 类型表id
   */
  @ApiModelProperty(value = "类型表id")
  @JsonSerialize(using = LongJsonSerializer.class)
  private Long categoryId;
  /**
   * 子类型表id
   */
  @ApiModelProperty(value = "子类型表id")
  @JsonSerialize(using = LongJsonSerializer.class)
  private Long subCategoryId;
  /**
   * 金额
   */
  @ApiModelProperty(value = "金额")
  private BigDecimal amount;
  /**
   * 成员表id
   */
  @ApiModelProperty(value = "成员表id")
  @JsonSerialize(using = LongJsonSerializer.class)
  private Long memberId;
  /**
   * 用户表id
   */
  @ApiModelProperty(value = "用户表id")
  @JsonSerialize(using = LongJsonSerializer.class)
  private Long userId;
  /**
   * 备注
   */
  @ApiModelProperty(value = "备注")
  private String remark;
  /**
   * 时间
   */
  @JsonSerialize(using = LongDateSerializer.class)
  private Long date;

}
