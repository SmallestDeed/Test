package com.sandu.search.entity.elasticsearch.po;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in PM 3:42 2018/7/31 0031
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title: 商品元数据实体类
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/7/31 0031PM 3:42
 */
@Data
public class BaseGoodsSpuPo implements Serializable{


    private static final long serialVersionUID = 1111025098403613012L;
    private Integer id ; //商品spuID
    private String spuCode; //商品spu编码
    private Integer companyId;//公司ID
    private String spuName; //商品名称
    private Integer picId; //商品缩略图
    private String picIds; //商品图片列表
    private Integer isSpecialOffer;//商品是否特卖,0:否,1:是
    private Integer isPresell; //商品是否预售,0:否,1:是
    private String picPath;//商品的spu缩略图地址
    private Date getTime;//获得时间
    private Integer isPutaway;//是否上架
    private Date gmtCreate;//创建时间
    private Date gmtModified;//修改时间
    //数据是否删除(0:未删除, 1:已删除)
    private Integer dataIsDelete;

}
