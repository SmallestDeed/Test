package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品传输对象
 *
 * @date 20171211
 * @auth pengxuangang
 */
@Data
public class ProductDTO implements Serializable {
    private static final long serialVersionUID = -4858433886820551470L;

    //产品ID
    private Integer id;
    //产品编码
    private String productCode;

}
