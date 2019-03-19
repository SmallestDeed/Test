package com.sandu.search.service.elasticsearch.impl;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.dto.DeleteRequestDTO;
import com.sandu.search.entity.elasticsearch.dto.GetRequestDTO;
import com.sandu.search.entity.elasticsearch.dto.IndexRequestDTO;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.ShouldMatchSearch;
import com.sandu.search.entity.elasticsearch.search.SortOrderObject;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 搜索引擎服务
 *
 * @date 20171211
 * @auth pengxuangang
 */
@Slf4j
@Service("elasticSearchService")
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final static String CLASS_LOG_PREFIX = "搜索引擎服务:";
    private final RestHighLevelClient restHighLevelClient;
    private Header[] defaultHeaders = new Header[0];

    @Autowired
    public ElasticSearchServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public boolean index(IndexRequestDTO indexRequestDTO) throws ElasticSearchException {
        return this.index(indexRequestDTO, null);
    }

    @Override
    public boolean index(IndexRequestDTO indexRequestDTO, Header... headers) throws ElasticSearchException {
        IndexResponse indexResponse;
        try {
            indexResponse = restHighLevelClient.index(indexRequestDTO, (null == headers ? defaultHeaders : headers));
        } catch (IOException e) {
            log.error(CLASS_LOG_PREFIX + "索引数据异常:IndexRequest:{},IOException:{}.", indexRequestDTO.toString(), e);
            throw new ElasticSearchException(CLASS_LOG_PREFIX + "索引数据异常:IndexRequest:" + indexRequestDTO.toString() + ",IOException:" + e);
        }

        //索引状态判断--用于后期索引计数
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            log.info(CLASS_LOG_PREFIX + "索引创建数据成功!IndexRequestDTO:{}", indexRequestDTO);
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            log.info(CLASS_LOG_PREFIX + "索引更新数据成功!IndexRequestDTO:{}", indexRequestDTO);
        }

        return true;
    }

    @Override
    public Object get(GetRequestDTO getRequestDTO, Class clazz) throws ElasticSearchException {
        return this.get(getRequestDTO, clazz, null);
    }

    @Override
    public Object get(GetRequestDTO getRequestDTO, Class clazz, Header... headers) throws ElasticSearchException {

        Object resultObj = null;
        GetResponse getResponse;
        try {
            getResponse = restHighLevelClient.get(getRequestDTO, (null == headers ? defaultHeaders : headers));
        } catch (IOException e) {
            log.error(CLASS_LOG_PREFIX + "获取数据异常:GetRequest:{},IOException:{}.", getRequestDTO.toString(), e);
            throw new ElasticSearchException(CLASS_LOG_PREFIX + "获取数据:GetRequest:" + getRequestDTO.toString() + ",IOException:" + e);
        }

        //检查数据是否获取到数据
        if (getResponse.isExists()) {
            String sourceAsString = getResponse.getSourceAsString();
            log.info(CLASS_LOG_PREFIX + "已获取数据:GetRequest:{}, JsonData:{}", getRequestDTO.toString(), sourceAsString);
            if (!StringUtils.isEmpty(sourceAsString)) {
                resultObj = JsonUtil.fromJson(sourceAsString, clazz);
            }
        } else {
            log.warn(CLASS_LOG_PREFIX + "获取数据失败:GetRequest:{}, GetResponse:{}", getRequestDTO.toString(), getResponse.toString());
        }

        return resultObj;
    }

    @Override
    public boolean delete(DeleteRequestDTO deleteRequestDTO) throws ElasticSearchException {
        return this.delete(deleteRequestDTO, null);
    }

    @Override
    public boolean delete(DeleteRequestDTO deleteRequestDTO, Header... headers) throws ElasticSearchException {
        try {
            restHighLevelClient.delete(deleteRequestDTO, (null == headers ? defaultHeaders : headers));
        } catch (IOException e) {
            log.error(CLASS_LOG_PREFIX + "删除数据异常:DeleteRequestDTO:{},IOException:{}.", deleteRequestDTO.toString(), e);
            throw new ElasticSearchException(CLASS_LOG_PREFIX + "删除数据异常:DeleteRequestDTO:" + deleteRequestDTO.toString() + ",IOException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "删除数据完成:DeleteRequestDTO:{}", deleteRequestDTO.toString());

        return true;
    }

    @SuppressWarnings("all")
    @Override
    public BulkStatistics bulk(List<Object> bulkRequestList, Header... headers) throws ElasticSearchException {
        //统计数据
        BulkStatistics bulkStatistics = new BulkStatistics();
        //批量请求数据组合
        BulkRequest bulkRequest = new BulkRequest();

        //分类数据，统计有效数据量
        bulkRequestList.forEach(requestObj -> {
            if (requestObj instanceof IndexRequest) {
                bulkRequest.add((IndexRequest) requestObj);
                bulkStatistics.setIndexCount(bulkStatistics.getIndexCount() + 1);
            } else if (requestObj instanceof DeleteRequest) {
                bulkRequest.add((DeleteRequest) requestObj);
                bulkStatistics.setDeleteCount(bulkStatistics.getDeleteCount() + 1);
            } else if (requestObj instanceof UpdateRequest) {
                bulkRequest.add((UpdateRequest) requestObj);
                bulkStatistics.setUpdateCount(bulkStatistics.getUpdateCount() + 1);
            } else {
                log.warn(CLASS_LOG_PREFIX + "批量操作:不支持的request类型:requestObj:{}", requestObj);
            }
        });

        //存储任务数
        bulkStatistics.setTotalCount(bulkStatistics.getIndexCount() + bulkStatistics.getDeleteCount() + bulkStatistics.getUpdateCount());
        log.info(CLASS_LOG_PREFIX + "批量操作:提交任务数:{},有效任务数:{}", bulkRequestList.size(), bulkStatistics.getTotalCount());

        //提交数据
        BulkResponse bulkResponse;
        try {
            bulkResponse = restHighLevelClient.bulk(bulkRequest, (null == headers ? defaultHeaders : headers));
        } catch (IOException e) {
            log.error(CLASS_LOG_PREFIX + "批量操作异常:BulkRequest:{},IOException:{}.", bulkRequest.toString(), e);
            throw new ElasticSearchException(CLASS_LOG_PREFIX + "批量操作异常:BulkRequest:" + bulkRequest.toString() + ",IOException:" + e);
        }

        //处理结果
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();

            if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                    || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                IndexResponse indexResponse = (IndexResponse) itemResponse;
                if (null != indexResponse && (RestStatus.OK == indexResponse.status() || RestStatus.CREATED == indexResponse.status())) {
                    bulkStatistics.setIndexSuccessCount(bulkStatistics.getIndexSuccessCount() + 1);
                } else {
                    log.warn(CLASS_LOG_PREFIX + "批量操作-索引异常:IndexResponse:{}, Response:{}.", (null == indexResponse ? "null" : indexResponse.toString()), bulkItemResponse.getFailureMessage());
                    bulkStatistics.setIndexFailCount(bulkStatistics.getIndexFailCount() + 1);
                }

            } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                if (RestStatus.OK == updateResponse.status()) {
                    bulkStatistics.setUpdateSuccessCount(bulkStatistics.getUpdateSuccessCount() + 1);
                } else {
                    log.warn(CLASS_LOG_PREFIX + "批量操作-更新异常:UpdateResponse:{}", updateResponse.toString());
                    bulkStatistics.setUpdateFailCount(bulkStatistics.getUpdateFailCount() + 1);
                }

            } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                if (RestStatus.OK == deleteResponse.status()) {
                    bulkStatistics.setDeleteSuccessCount(bulkStatistics.getDeleteSuccessCount() + 1);
                } else {
                    log.warn(CLASS_LOG_PREFIX + "批量操作-删除异常:DeleteResponse:{}", deleteResponse.toString());
                    bulkStatistics.setDeleteFailCount(bulkStatistics.getDeleteFailCount() + 1);
                }
            }
        }
        //总条数增加
        bulkStatistics.setTotalSuccessCount(bulkStatistics.getIndexSuccessCount() + bulkStatistics.getDeleteSuccessCount() + bulkStatistics.getUpdateSuccessCount());
        bulkStatistics.setTotalFailCount(bulkStatistics.getIndexFailCount() + bulkStatistics.getDeleteFailCount() + bulkStatistics.getUpdateFailCount());

        return bulkStatistics;
    }

    @Override
    public SearchObjectResponse search(List<QueryBuilder> matchQueryBuilderList, List<QueryBuilder> noMatchQueryBuilderList, List<ShouldMatchSearch> shouldMatchSearchList, List<AggregationBuilder> aggregationBuilderList, SortOrderObject sortOrderObject, int start, int dataSize, String index) throws ElasticSearchException {

        if (StringUtils.isEmpty(index)) {
            log.error(CLASS_LOG_PREFIX + "必须参数:索引名为空.");
            throw new ElasticSearchException(CLASS_LOG_PREFIX + "必须参数:索引名为空.");
        }

        //搜索查询条件组建
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //多条件匹配
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //搜索匹配数据
        if (null != matchQueryBuilderList && 0 < matchQueryBuilderList.size()) {
            matchQueryBuilderList.forEach(boolQueryBuilder::must);
        }
        //排除指定数据
        if (null != noMatchQueryBuilderList && 0 < noMatchQueryBuilderList.size()) {
            noMatchQueryBuilderList.forEach(boolQueryBuilder::mustNot);
        }
        //或匹配数据
        if (null != shouldMatchSearchList && 0 < shouldMatchSearchList.size()) {
            shouldMatchSearchList.forEach(shouldMatchSearch -> boolQueryBuilder.should(shouldMatchSearch.getQueryBuilder()).minimumShouldMatch(shouldMatchSearch.getMinimumShouldMatch()));
        }
        //聚合条件
        if (null != aggregationBuilderList && 0 < aggregationBuilderList.size()) {
            aggregationBuilderList.forEach(sourceBuilder::aggregation);
        }

        //设置查询数据大小/超时/查询条件
        sourceBuilder.from(start > 0 ? start : IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START);
        sourceBuilder.size(0 == dataSize ? IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE : dataSize);
        sourceBuilder.timeout(new TimeValue(5, TimeUnit.SECONDS));
        if (null != sortOrderObject) {
            //构造排序对象
            FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort(sortOrderObject.getFieldName());
            fieldSortBuilder.sortMode(sortOrderObject.getSortMode());
            fieldSortBuilder.order(sortOrderObject.getSortOrder());
            sourceBuilder.sort(fieldSortBuilder);
        }
        sourceBuilder.query(boolQueryBuilder);

        //设置查询索引
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(sourceBuilder);

        log.info(CLASS_LOG_PREFIX + "搜索数据DSL:{}", sourceBuilder.toString());
        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            log.error(CLASS_LOG_PREFIX + "搜索数据异常..:SearchRequest:{}, Exception:{}", searchRequest.toString(), e.getMessage());
            throw new ElasticSearchException(CLASS_LOG_PREFIX + "搜索数据异常..:SearchRequest:" + searchRequest.toString() + ", Exception:" + e.getMessage());
        }

        //组装结果
        List<Object> objectDataList = new ArrayList<>();
        Iterator<SearchHit> searchHitIterator = searchResponse.getHits().iterator();
        while (searchHitIterator.hasNext()) {
            SearchHit searchHit = searchHitIterator.next();
            if (!StringUtils.isEmpty(searchHit.getSourceAsString())) {
                //检查索引类型
                Class clazz = null;
                switch (index) {
                    //产品
                    case IndexConstant.INDEX_PRODUCT_INFO_ALIASES :
                        clazz = ProductIndexMappingData.class;
                        break;
                    //推荐方案
                    case IndexConstant.RECOMMENDATION_PLAN_INFO :
                        clazz = RecommendationPlanIndexMappingData.class;
                        break;
                    default:
                        log.error(CLASS_LOG_PREFIX + "无法识别的索引类型:Index:{}", index);
                        break;
                }

                //格式化数据
                Object object;
                try {
                    object = JsonUtil.fromJson(searchHit.getSourceAsString(), clazz);
                } catch (Exception e) {
                    log.error(CLASS_LOG_PREFIX + "解析Json数据异常,跳过此条数据. JsonStr:{}, Exception:{}.", searchHit.getSourceAsString(), e.toString());
                    continue;
                }
                objectDataList.add(object);
            }
        }

        return new SearchObjectResponse(objectDataList, searchResponse.getHits().totalHits, searchResponse.getAggregations());
    }

    @Override
    public UpdateResponse execUpdateRequest(UpdateRequest quest) {
        UpdateResponse response = null;
        try {
            response = restHighLevelClient.update(quest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}