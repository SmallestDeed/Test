package com.sandu.search.service.elasticsearch;

import com.sandu.search.entity.elasticsearch.dto.DeleteRequestDTO;
import com.sandu.search.entity.elasticsearch.dto.GetRequestDTO;
import com.sandu.search.entity.elasticsearch.dto.IndexRequestDTO;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.ShouldMatchSearch;
import com.sandu.search.entity.elasticsearch.search.SortOrderObject;
import com.sandu.search.entity.response.ElasticSearchResponse;
import com.sandu.search.exception.ElasticSearchException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

/**
 * 搜索引擎服务
 *
 * @date 20171211
 * @auth pengxuangang
 */
public interface ElasticSearchService {

    /**
     * 索引数据
     *
     * @param indexRequestDTO 索引对象
     * @return
     * @throws ElasticSearchException
     */
    boolean index(IndexRequestDTO indexRequestDTO) throws ElasticSearchException;

    /**
     * 索引数据
     *
     * @param indexRequestDTO 索引对象
     * @param headers         请求头
     * @return
     * @throws ElasticSearchException
     */
    boolean index(IndexRequestDTO indexRequestDTO, org.apache.http.Header... headers) throws ElasticSearchException;

    /**
     * 获取数据
     *
     * @param getRequestDTO 请求对象
     * @return
     * @throws ElasticSearchException
     */
    Object get(GetRequestDTO getRequestDTO, Class clazz) throws ElasticSearchException;

    /**
     * 获取数据
     *
     * @param getRequestDTO 请求对象
     * @param headers       请求头
     * @return
     * @throws ElasticSearchException
     */
    Object get(GetRequestDTO getRequestDTO, Class clazz, org.apache.http.Header... headers) throws ElasticSearchException;

    /**
     * 删除数据
     *
     * @param deleteRequestDTO 索引对象
     * @return
     * @throws ElasticSearchException
     */
    boolean delete(DeleteRequestDTO deleteRequestDTO) throws ElasticSearchException;

    /**
     * 删除数据
     *
     * @param deleteRequestDTO 索引对象
     * @param headers          请求头
     * @return
     * @throws ElasticSearchException
     */
    boolean delete(DeleteRequestDTO deleteRequestDTO, org.apache.http.Header... headers) throws ElasticSearchException;

    /**
     * 批量操作
     *
     * @param bulkRequestList 操作List
     * @param headers         请求头
     * @return
     * @throws ElasticSearchException
     */
    BulkStatistics bulk(List<Object> bulkRequestList, org.apache.http.Header... headers) throws ElasticSearchException;

    /**
     * 搜索产品分类数据
     *
     * @param matchQueryBuilderList       搜索条件
     * @param noMatchQueryBuilderList     不匹配的搜索条件
     * @param shouldMatchSearchList       或匹配条件
     * @param aggregationBuilderList      聚合条件
     * @param sortOrderObjectList         排序对象
     * @param start                       数据起始数
     * @param dataSize                    数据条数
     * @param index                       索引
     * @return
     * @throws ElasticSearchException
     */
    SearchObjectResponse search(List<QueryBuilder> matchQueryBuilderList, List<QueryBuilder> noMatchQueryBuilderList, List<ShouldMatchSearch> shouldMatchSearchList, List<AggregationBuilder> aggregationBuilderList, List<SortOrderObject> sortOrderObjectList, int start, int dataSize, String index) throws ElasticSearchException;

    /**
     * 全文搜索
     * 
     * @author huangsongbo
     * @param sourceBuilder 搜索条件build
     * @param indexProductInfo 索引名
     * @return
     * @throws ElasticSearchException 
     */
	Object search(SearchSourceBuilder sourceBuilder, String indexProductInfo) throws ElasticSearchException;

    /**
     * @author xiaoxc
     * @param sourceBuilder
     * @param index
     * @return
     * @throws ElasticSearchException
     */
    ElasticSearchResponse searchData(SearchSourceBuilder sourceBuilder, String index) throws ElasticSearchException;
}