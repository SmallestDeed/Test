package com.sandu.search.entity.elasticsearch.search.product;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 一键装修搜索产品接口参数
 * @author huangsongbo
 *
 */
@Data
public class ProductSearchForOneKeyDTO implements Serializable{

	private static final long serialVersionUID = -6708429836817094050L;
	
	/**
	 * 产品大类value
	 */
	private String productTypeValue;
	
	/**
	 * 产品小类value
	 */
	private Integer productSmallTypeValue;
	
	/**
	 * 产品长度
	 */
	private String productLength;
	
	/**
	 * 产品宽度
	 */
	private String productWidth;
	
	/**
	 * 产品高度
	 */
	private String productHeight;
	
	/**
	 * 产品长度范围start
	 */
	private Integer productLengthStartInteger;
	
	/**
	 * 产品长度范围end
	 */
	private Integer productLengthEndInteger;
	
	/**
	 * 尺寸代码
	 */
	private String measureCode;
	
	/**
	 * 款式id
	 */
	private Integer styleId;
	
	/**
	 * 白膜id(定制产品对应的白膜id)
	 */
	private String bmIds;
	
	/**
	 * 墙体类型
	 */
	private String wallType;
	
	/**
	 * 布局标识
	 */
	private String productSmallpoxIdentify;
	
	/**
	 * 布局标识list
	 */
	private List<String> identifyList;
	
	/**
	 * 产品过滤属性
	 */
	/*private List<ProductPropsSimple> productFilterPropList;*/
	private Map<String, String> productFilterAttributeMap;
	
	/**
	 * 是否是背景墙
	 */
	private boolean isBeijing;
	
	/**
	 * 系列id
	 */
	private Integer seriesId;
	
	/**
	 * 产品小类list(排除)
	 */
	private List<Integer> excludeSmallTypeValueList;
	
	/**
	 * 小类list
	 */
	private List<Integer> smallTypeValueListForShowAll;
	
	/**
	 * 产品状态list
	 */
	private List<Integer> putawayStateList;
	
	private Integer isDeleted;
	
	// 排序条件 ->start
	
	/**
	 * 排序条件:为该小类value的产品排前面
	 */
	private Integer orderSmallTypeValue;
	
	/**
	 * 排序条件:为该styleId的产品排前面
	 */
	private Integer orderStyleId;
	
	/**
	 * 排序条件:等于该brandId的产品排前面
	 */
	private Integer orderBrandId;
	
	/**
	 * 排序条件:等于该productModelNumber的产品排前面
	 */
	private String orderProductModelNumber;
	
	/**
	 * 排序条件:等于该布局标识的产品排前面
	 */
	private String orderProductSmallpoxIdentify;
	
	/**
	 * 排序条件:产品长度最接近orderAbsProuductLength的产品排前面
	 */
	private Integer orderAbsProuductLength;
	
	// 排序条件 ->end
	
	private Integer start;
	
	private Integer limit;
	
	/**
	 * 产品长度下限(搜索条件)
	 */
	private Integer productHeightStart;
	
	/**
	 * 产品长度上限(搜索条件)
	 */
	private Integer productHeightEnd;
	
}
