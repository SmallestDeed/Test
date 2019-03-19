package com.sandu.search.dao;


import com.sandu.search.entity.product.dto.ProductProps;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version V1.0
 * @Title: ProductPropsMapper.java
 * @Description:产品属性-产品属性表Mapper
 * @createAuthor pandajun
 * @CreateDate 2015-09-01 10:40:03
 */

@Repository
@Transactional
public interface ProductPropsMapper {

    ProductProps selectByPrimaryKey(Integer id);
}
