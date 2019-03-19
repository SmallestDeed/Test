package com.sandu.search;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

public class ElasticSearchJavaApiTest {

	@org.junit.Test
	public void test0001() {
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termQuery("productTypeValue", 3));
		boolQueryBuilder.must(QueryBuilders.termQuery("productTypeSmallValue", 2));
		boolQueryBuilder.mustNot(QueryBuilders.termQuery("productBrandId", 178));
		sourceBuilder.postFilter(boolQueryBuilder);
		Script script = new Script("Math.abs(doc['productLength'].value - 55)");
		ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortType.NUMBER).order(SortOrder.ASC);
		sourceBuilder.sort(scriptSortBuilder);
		System.out.println(sourceBuilder.toString());
	}
	
}
