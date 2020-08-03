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
 * 用户表
 * </p>
 *
 * @description: 用户表
 * @author: fenmi
 * @date: Created in 2020-08-03 10:38:53
 */
@ApiModel("用户表")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDTO extends BaseDTO implements Serializable {

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
