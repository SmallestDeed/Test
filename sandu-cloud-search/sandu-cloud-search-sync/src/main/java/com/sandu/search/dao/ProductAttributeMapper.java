package com.sandu.search.dao;

import com.sandu.search.entity.product.dto.ProductAttribute;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @version V1.0
 * @Title: ProductAttributeMapper.java
 * @Description:产品模块-产品属性Mapper
 * @createAuthor pandajun
 * @CreateDate 2015-09-01 13:17:36
 */
@Repository
@Transactional
public interface ProductAttributeMapper {


    int selectCount(Integer productId);

    List<ProductAttribute> selectPaginatedList(Integer productId);

}
