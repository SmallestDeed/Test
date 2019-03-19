package com.sandu.search.service.index.impl;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.util.Utils;
import com.sandu.search.dao.ProductIndexDao;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.entity.elasticsearch.po.ProductTexturePo;
import com.sandu.search.entity.product.dto.SplitTextureDTO;
import com.sandu.search.entity.product.dto.SplitTextureInfoDTO;
import com.sandu.search.exception.ProductIndexException;
import com.sandu.search.service.index.ProductIndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类产品索引服务
 *
 * @date 20171212
 * @auth pengxuangang
 */
@Slf4j
@Service("productIndexService")
public class ProductIndexServiceImpl implements ProductIndexService {

    private final static String CLASS_LOG_PREFIX = "分类产品索引服务:";

    private final ProductIndexDao productIndexDao;

    @Autowired
    public ProductIndexServiceImpl(ProductIndexDao productIndexDao) {
        this.productIndexDao = productIndexDao;
    }

    @Override
    public List<ProductPo> queryProductPoList(int start, int limit) throws ProductIndexException {

        //初始化数据条数
        if (0 == limit) {
            limit = IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT;
        }

        //查询产品信息
        log.info(CLASS_LOG_PREFIX + "正在查询产品信息第{}-{}条.", start, (start + limit));
        List<ProductPo> productPoList;
        try {
            productPoList = productIndexDao.queryProductPoList(start, limit);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取产品数据第{}-{}条失败,Exception:" + e, start, (start + limit));
            throw new ProductIndexException(CLASS_LOG_PREFIX + "获取产品数据第" + start + "-" + (start + limit) + "条失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品信息完成,List<ProductPo>长度:{}.", productPoList.size());

        return productPoList;
    }


    @Override
    public List<ProductPo> queryProductPoListByProductIdList(List<Integer> productIdList) throws ProductIndexException {
        if (null == productIdList || 0 >= productIdList.size()) {
            return null;
        }

        //查询产品信息
        log.info(CLASS_LOG_PREFIX + "正在查询产品信息,productIdList:{}.", JsonUtil.toJson(productIdList));
        List<ProductPo> productPoList;
        try {
            productPoList = productIndexDao.queryProductPoListByProductIdList(productIdList);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取产品信息失败,productIdList:{}, Exception:{}.", JsonUtil.toJson(productIdList), e);
            throw new ProductIndexException(CLASS_LOG_PREFIX + "获取产品信息失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品信息完成,List<ProductPo>长度:{}.", productPoList.size());

        return productPoList;
    }

    @Override
    public Map<String, Object> dealWithSplitTextureInfo(Integer baseProductId, String splitTexturesInfo, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        Integer isSplit = new Integer(0);
        List<SplitTextureDTO> splitTexturesChooseList = new ArrayList<SplitTextureDTO>();
        List<SplitTextureDTO> splitTexturesInfoList = new ArrayList<SplitTextureDTO>();

        List<SplitTextureInfoDTO> splitTextureList = JsonUtil.fromJson(splitTexturesInfo, new TypeToken<List<SplitTextureInfoDTO>>(){}.getType() );

        try {
            if (splitTextureList != null && splitTextureList.size() >= 1) {
                for (SplitTextureInfoDTO splitTextureInfoDTO : splitTextureList) {

                    //判断材质区域高宽是否存在、不存在默认80/80
                    if(null == splitTextureInfoDTO.getHeight() || 0 == splitTextureInfoDTO.getHeight())
                        splitTextureInfoDTO.setHeight(80);
                    if(null == splitTextureInfoDTO.getWidth() || 0 == splitTextureInfoDTO.getWidth())
                        splitTextureInfoDTO.setWidth(80);

                    /*处理默认材质*/
                    if (StringUtils.equals("choose", type) || StringUtils.equals("all", type)) {
                        Integer defaultId = splitTextureInfoDTO.getDefaultId();
                        List<SplitTextureDTO.ResTextureDTO> resTextureDTOList = new ArrayList<SplitTextureDTO.ResTextureDTO>();
                        if (defaultId != null && defaultId > 0) {
                            // ResTexture resTexture = indexGroupProductService.getResTexture(defaultId);
                            // 从缓存元数据中获取材质信息
                            // ProductTexturePo groupProductTexture = productTextureMetaDataStorage.getGroupProductTexture(defaultId);
                            // if (groupProductTexture != null && groupProductTexture.getId() > 0) {
                                // SplitTextureDTO.ResTextureDTO resTextureDTO = indexGroupProductService.fromResTexture(groupProductTexture);
                                // resTextureDTO.setKey(splitTextureInfoDTO.getKey());
                                // resTextureDTO.setProductId(baseProductId);
                                // if( resTextureDTO.getTextureWidth() == 0 || resTextureDTO.getTextureWidth() == null ){
                                //     resTextureDTO.setTextureWidth(80); //徐扬确认。如果材质长度为空/0则给默认80
                                // }
                                // if( resTextureDTO.getTextureHeight() == 0 || resTextureDTO.getTextureHeight() == null ){
                                //     resTextureDTO.setTextureHeight(80); //徐扬确认。如果材质长度为空/0则给默认80
                                // }
                                // resTextureDTOList.add(resTextureDTO);
                            // }
                        }
                        splitTexturesChooseList.add(new SplitTextureDTO(splitTextureInfoDTO.getKey(), splitTextureInfoDTO.getName(),splitTextureInfoDTO.getTextureRegionName(),splitTextureInfoDTO.getHeight(),splitTextureInfoDTO.getWidth(), resTextureDTOList));
                    }
                    /*处理默认材质->end*/

                    /*处理可选材质*/
                    if (StringUtils.equals("info", type) || StringUtils.equals("all", type)) {
                        String textureIdsStr = splitTextureInfoDTO.getTextureIds();
                        List<SplitTextureDTO.ResTextureDTO> resTextureDTOList = new ArrayList<SplitTextureDTO.ResTextureDTO>();
                        List<String> textureIdStrList = Utils.getListFromStr(textureIdsStr);
                        if (textureIdStrList != null && textureIdStrList.size() > 0) {
                            // 从缓存的元数据中获取产品材质集合
                            // List<ProductTexturePo> groupProductTextureList = productTextureMetaDataStorage.getTextureByIds(textureIdStrList);
                            // 按照textureIdStrList排序 ->start

                            // textureIdStrList:[406, 407, 409, 410]
                            // 将默认材质和textureIdStrList第一个元素互换位置 ->start
                            Integer defaultId = splitTextureInfoDTO.getDefaultId();
                            if(defaultId != null){
                                int index = textureIdStrList.indexOf(defaultId + "");
                                if(index != -1){
                                    String item = textureIdStrList.get(0);
                                    textureIdStrList.set(0, defaultId + "");
                                    textureIdStrList.set(index, item);
                                }
                            }
                            List<ProductTexturePo> listNew = new ArrayList<ProductTexturePo>();
                            // 将默认材质和textureIdStrList第一个元素互换位置 ->end
                            if(textureIdStrList != null && textureIdStrList.size() > 0){
                                for(int i = 0; i < textureIdStrList.size(); i ++){
                                    listNew.add(null);
                                }
                            }
                            // if(groupProductTextureList != null && groupProductTextureList.size() > 0){
                            //     for(ProductTexturePo productTexturePo : groupProductTextureList){
                            //         if(textureIdStrList.indexOf("" + productTexturePo.getId()) != -1){
                            //             listNew.set(textureIdStrList.indexOf("" + productTexturePo.getId()), productTexturePo);
                            //         }
                            //     }
                            // }
                            // groupProductTextureList = listNew;
                            // // 按照textureIdStrList排序 ->end
                            // for (ProductTexturePo productTexturePo : groupProductTextureList) {
                            //     if(productTexturePo == null){
                            //         continue;
                            //     }
                                // SplitTextureDTO.ResTextureDTO resTextureDTO = indexGroupProductService.fromResTexture(productTexturePo);
                                // resTextureDTO.setKey(splitTextureInfoDTO.getKey());
                                // resTextureDTO.setProductId(baseProductId);
                                // if( resTextureDTO.getTextureWidth() == 0 || resTextureDTO.getTextureWidth() == null ){
                                //     resTextureDTO.setTextureWidth(80); //徐扬确认。如果材质长度为空/0则给默认80
                                // }
                                // if( resTextureDTO.getTextureHeight() == 0 || resTextureDTO.getTextureHeight() == null ){
                                //     resTextureDTO.setTextureHeight(80); //徐扬确认。如果材质长度为空/0则给默认80
                                // }
                                // resTextureDTOList.add(resTextureDTO);
                        //     }

                            splitTexturesInfoList.add(new SplitTextureDTO(splitTextureInfoDTO.getKey(), splitTextureInfoDTO.getName(),splitTextureInfoDTO.getTextureRegionName(),splitTextureInfoDTO.getHeight(),splitTextureInfoDTO.getWidth(), resTextureDTOList));
                        }
                    }

                    /*处理可选材质->end*/
                }
                isSplit = new Integer(1);
                map.put("isSplit", isSplit);
                map.put("splitTexturesChoose", splitTexturesChooseList);
                map.put("splitTexturesInfo", splitTexturesInfoList);
            }
        }catch(Exception e){
            log.warn("------baseProduct的SplitTextureInfo信息格式错误,SplitTextureInfo:" + splitTexturesInfo);
        }
        return map;
    }
}
