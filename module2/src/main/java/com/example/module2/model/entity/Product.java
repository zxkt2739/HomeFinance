package com.example.module2.model.entity;

import com.example.core.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@ApiModel(description = "")
public class Product extends BaseEntity implements Serializable {

  /**
   * 类型id
   */
  @ApiModelProperty(value = "类型id")
  private Long categoryId;
  /**
   * 商铺id
   */
  @ApiModelProperty(value = "商铺id")
  private Long merchantId;
  /**
   * 名称
   */
  @ApiModelProperty(value = "名称")
  private String name;
  /**
   * 价格
   */
  @ApiModelProperty(value = "价格")
  private BigDecimal price;
  /**
   * 原价
   */
  @ApiModelProperty(value = "原价")
  private BigDecimal originalPrice;
  /**
   * 库存
   */
  @ApiModelProperty(value = "库存")
  private Integer stock;
  /**
   * 描述
   */
  @ApiModelProperty(value = "描述")
  private String description;
  /**
   * 上架状态：0-下架，1-上架
   */
  @ApiModelProperty(value = "上架状态：0-下架，1-上架")
  private Integer state;

}
