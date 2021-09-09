package com.mosh.srb.core.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/9/9
 */
@Data
@ApiModel(description="借款人搜索对象")
public class BorrowerQuery {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "身份证号")
    private String idCard;
}