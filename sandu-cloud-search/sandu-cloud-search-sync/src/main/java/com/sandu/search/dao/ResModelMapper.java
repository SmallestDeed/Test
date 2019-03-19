package com.sandu.search.dao;

import com.sandu.search.entity.product.dto.ResModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version V1.0
 * @Title: ResModelMapper.java
 * @Description:系统-模型资源库Mapper
 * @createAuthor pandajun
 * @CreateDate 2015-05-19 16:05:22
 */
@Repository
@Transactional
public interface ResModelMapper {


    ResModel selectByPrimaryKey(Integer id);

}
