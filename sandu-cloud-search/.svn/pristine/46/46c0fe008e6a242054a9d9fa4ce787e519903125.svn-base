package com.sandu.search.service.product.dubbo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sandu.search.common.constant.ProductTypeValue;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.search.common.constant.QueryConditionField;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchForOneKeyDTO;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.product.dubbo.ProductSearchOpenService;

import lombok.extern.slf4j.Slf4j;

@Service("productSearchOpenService")
@Slf4j
public class ProductSearchOpenServiceImpl implements ProductSearchOpenService {

	private final static String CLASS_LOG_PREFIX = "产品搜索服务(consumer调用):";
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductIndexMappingData> searchProduct(ProductSearchForOneKeyDTO productSearchForOneKeyDTO) throws ElasticSearchException {
		// 参数验证 ->start
		if(productSearchForOneKeyDTO == null) {
			log.error(CLASS_LOG_PREFIX + "productSearchForOneKeyDTO = null");
			return null;
		}
		// 参数验证 ->end
		
		// 查询条件处理成dsl语句 ->start
		
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		List<ScriptSortBuilder> scriptSortBuilderList = new ArrayList<ScriptSortBuilder>();
		List<FieldSortBuilder> fieldSortBuilderList = new ArrayList<FieldSortBuilder>();
		/*SortBuilders.scoreSort();*/
		
		if(StringUtils.isNotBlank(productSearchForOneKeyDTO.getProductTypeValue())) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, Integer.valueOf(productSearchForOneKeyDTO.getProductTypeValue())));
		}
		if(productSearchForOneKeyDTO.getProductSmallTypeValue() != null) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, Integer.valueOf(productSearchForOneKeyDTO.getProductSmallTypeValue())));
		}
		if(StringUtils.isNotBlank(productSearchForOneKeyDTO.getProductLength())) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_LENGTH, Integer.valueOf(productSearchForOneKeyDTO.getProductLength())));
		}
		if(StringUtils.isNotBlank(productSearchForOneKeyDTO.getProductWidth())) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_WIDTH, Integer.valueOf(productSearchForOneKeyDTO.getProductWidth())));
		}
		if(StringUtils.isNotBlank(productSearchForOneKeyDTO.getProductHeight())) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT, Integer.valueOf(productSearchForOneKeyDTO.getProductHeight())));
		}
		if(productSearchForOneKeyDTO.getProductLengthStartInteger() != null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_LENGTH).gte(productSearchForOneKeyDTO.getProductLengthStartInteger()));
		}
		if(productSearchForOneKeyDTO.getProductLengthEndInteger() != null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_LENGTH).lte(productSearchForOneKeyDTO.getProductLengthEndInteger()));
		}
		// 布局标识现在暂时没用,所以先不管
		if(StringUtils.isNotBlank(productSearchForOneKeyDTO.getMeasureCode())) {
			
		}
		if(productSearchForOneKeyDTO.getStyleId() != null) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_STYLE_MODEL_ID, productSearchForOneKeyDTO.getStyleId()));
		}
		if(StringUtils.isNotBlank(productSearchForOneKeyDTO.getBmIds())) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BM_IDS, productSearchForOneKeyDTO.getBmIds()));
		}
		// 产品表中的墙体分类暂时没用,所以先不管
		if(StringUtils.isNotBlank(productSearchForOneKeyDTO.getWallType())) {
			/*boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_WALL_TYPE, productSearchForOneKeyDTO.getWallType()));*/
		}
		if(productSearchForOneKeyDTO.getSmallTypeValueListForShowAll() != null && productSearchForOneKeyDTO.getSmallTypeValueListForShowAll().size() > 0) {
			boolQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, productSearchForOneKeyDTO.getSmallTypeValueListForShowAll()));
		}
		if(productSearchForOneKeyDTO.getExcludeSmallTypeValueList() != null && productSearchForOneKeyDTO.getExcludeSmallTypeValueList().size() > 0) {
			boolQueryBuilder.mustNot(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, productSearchForOneKeyDTO.getExcludeSmallTypeValueList()));
		}
		// 布局标识现在暂时没用,所以先不管
		if(StringUtils.isNotBlank(productSearchForOneKeyDTO.getProductSmallpoxIdentify())) {
			
		}
		// 布局标识现在暂时没用,所以先不管
		if(productSearchForOneKeyDTO.getIdentifyList() != null && productSearchForOneKeyDTO.getIdentifyList().size() > 0) {
			
		}
		// 产品高度下限
		if(productSearchForOneKeyDTO.getProductHeightStart() != null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT).gte(productSearchForOneKeyDTO.getProductHeightStart()));
		}
		// 产品高度上限
		if(productSearchForOneKeyDTO.getProductHeightEnd() != null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT).lte(productSearchForOneKeyDTO.getProductHeightEnd()));
		}
		// 系列id
		if(productSearchForOneKeyDTO.getSeriesId() != null) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SERIES_ID, productSearchForOneKeyDTO.getSeriesId()));
		}
		// 产品状态(上下架)
		if(productSearchForOneKeyDTO.getPutawayStateList() != null && productSearchForOneKeyDTO.getPutawayStateList().size() > 0) {
			boolQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PUTAWAY_STATUS, productSearchForOneKeyDTO.getPutawayStateList()));
		}
		if(productSearchForOneKeyDTO.getIsDeleted() != null) {
			boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_IS_DELETE, productSearchForOneKeyDTO.getIsDeleted()));
		}
		// 过滤属性
		if(productSearchForOneKeyDTO.getProductFilterAttributeMap() != null && productSearchForOneKeyDTO.getProductFilterAttributeMap().size() > 0) {
			Map<String, String> productFilterAttributeMap = productSearchForOneKeyDTO.getProductFilterAttributeMap();
			if (null != productFilterAttributeMap && 0 < productFilterAttributeMap.size()) {
	            for (Map.Entry<String, String> entries : productFilterAttributeMap.entrySet()) {
	            	boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ATTRIBUTE_TYPE_FILTER_MAP + "." + entries.getKey(), entries.getValue()));
	            }
	        }
		}

		// add by zhangwj 不可替换产品过滤（无模型并且无贴图，只要有其一即表示可以替换）
		String productTypeValue = productSearchForOneKeyDTO.getProductTypeValue();
		if (ProductTypeValue.PRODUCT_TYPE_VALUE_CEILING != (StringUtils.isEmpty(productTypeValue)?-1:Integer.valueOf(productTypeValue))) {
			BoolQueryBuilder unReplaceProductBool = QueryBuilders.boolQuery();
			RangeQueryBuilder productModelIdRangeQuery = QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_MODEL_ID);
			productModelIdRangeQuery.gt(0);
			unReplaceProductBool.should(productModelIdRangeQuery);
			unReplaceProductBool.should(QueryBuilders.boolQuery().must(QueryBuilders.existsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_MATERIAL_LIST)));
			boolQueryBuilder.must(unReplaceProductBool);
		}

		// 
		if(productSearchForOneKeyDTO.getOrderStyleId() != null) {
			Script script = new Script("(doc['styleModelId'].value == " + productSearchForOneKeyDTO.getOrderStyleId() + ") ? 1 : 0");
			ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortType.NUMBER).order(SortOrder.DESC);
			scriptSortBuilderList.add(scriptSortBuilder);
		}
		// 
		if(productSearchForOneKeyDTO.getOrderSmallTypeValue() != null) {
			Script script = new Script("(doc['productTypeSmallValue'].value == " + productSearchForOneKeyDTO.getOrderSmallTypeValue() + ") ? 1 : 0");
			ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortType.NUMBER).order(SortOrder.DESC);
			scriptSortBuilderList.add(scriptSortBuilder);
		}
		if(productSearchForOneKeyDTO.getOrderAbsProuductLength() != null) {
			Script script = new Script("Math.abs(doc['productLength'].value - " + productSearchForOneKeyDTO.getOrderAbsProuductLength() + ")");
			ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortType.NUMBER).order(SortOrder.ASC);
			scriptSortBuilderList.add(scriptSortBuilder);
		}
		if(StringUtils.isNotEmpty(productSearchForOneKeyDTO.getOrderProductModelNumber())) {
			Script script = new Script("(doc['productModelNumber'].value == '" + productSearchForOneKeyDTO.getOrderProductModelNumber() + "') ? 1 : 0");
			ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortType.NUMBER).order(SortOrder.DESC);
			scriptSortBuilderList.add(scriptSortBuilder);
		}
		if(productSearchForOneKeyDTO.getOrderBrandId() != null) {
			Script script = new Script("(doc['productBrandId'].value == " + productSearchForOneKeyDTO.getOrderBrandId() + ") ? 2 : ((doc['productBrandId'].value == 178) ? 1 : 0)");
			ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortType.NUMBER).order(SortOrder.DESC);
			scriptSortBuilderList.add(scriptSortBuilder);
		}
		// 布局标识暂时没用,所以不管
		if(StringUtils.isNotEmpty(productSearchForOneKeyDTO.getOrderProductSmallpoxIdentify())) {
			
		}
		
		// 必要搜索条件 ->start
		// 分页 ->start
		if(productSearchForOneKeyDTO.getStart() == null || productSearchForOneKeyDTO.getLimit() == null) {
			productSearchForOneKeyDTO.setStart(0);
			productSearchForOneKeyDTO.setLimit(20);
		}
		sourceBuilder.from(productSearchForOneKeyDTO.getStart());
		sourceBuilder.size(productSearchForOneKeyDTO.getLimit());
		// 分页 ->end
		
		// 非白膜产品
		boolQueryBuilder.mustNot(QueryBuilders.prefixQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CODE, "baimo_"));
		// id倒序排序
		/*
		"sort": [
		  ...
		  ,{
		  "id": {
		    "order": "desc"
		  }
		}]
		*/
		fieldSortBuilderList.add(SortBuilders.fieldSort(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ID).order(SortOrder.DESC));
		// 必要搜索条件 ->end
		
		// 添加排序dsl ->start
		if(scriptSortBuilderList != null && scriptSortBuilderList.size() > 0) {
			scriptSortBuilderList.forEach(item -> sourceBuilder.sort(item));
		}
		if(fieldSortBuilderList != null && fieldSortBuilderList.size() > 0) {
			fieldSortBuilderList.forEach(item -> sourceBuilder.sort(item));
		}
		// 添加排序dsl ->end
		
		sourceBuilder.postFilter(boolQueryBuilder);
		log.info(CLASS_LOG_PREFIX + "DSL:{}", sourceBuilder.toString());
		
		// 查询条件处理成dsl语句 ->end
		
		return (List<ProductIndexMappingData>) elasticSearchService.search(sourceBuilder, IndexConstant.INDEX_PRODUCT_INFO_ALIASES);
	}

}
