package com.sandu.search.service.index.impl;

import com.sandu.search.common.util.Utils;
import com.sandu.search.dao.*;
import com.sandu.search.entity.elasticsearch.po.ProductTexturePo;
import com.sandu.search.entity.product.dto.*;
import com.sandu.search.service.index.IndexGroupProductService;
import com.sandu.search.storage.design.DesignRulesMetaDataStorage;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @version V1.0
 * @Title: IndexGroupProductServiceImpl.java
 * @Package com.nork.system.service.impl
 * @Description:系统模块-材质库ServiceImpl
 * @createAuthor pandajun
 * @CreateDate 2016-06-30 14:10:42
 */
@Service("indexGroupProductService")
public class IndexGroupProductServiceImpl implements IndexGroupProductService {

    private static final Logger logger = LoggerFactory.getLogger(IndexGroupProductServiceImpl.class);

    private ResTextureMapper resTextureMapper;
    private DesignRulesMetaDataStorage designRulesMetaDataStorage;

    @Autowired
    public void setResTextureMapper(ResTextureMapper resTextureMapper,
                                    DesignRulesMetaDataStorage designRulesMetaDataStorage) {
        this.resTextureMapper = resTextureMapper;
        this.designRulesMetaDataStorage = designRulesMetaDataStorage;
    }

    @Autowired
    private ResModelMapper resModelMapper;
    @Autowired
    private ResPicMapper resPicMapper;
    @Autowired
    private BaseBrandMapper baseBrandMapper;
    @Autowired
    private ProductPropsMapper productPropsMapper;


    @Override
    public SplitTextureDTO.ResTextureDTO fromResTexture_old(ProductTexturePo groupProductTexture) {
        Integer picId = groupProductTexture.getPicId();
        Integer normalPicId = groupProductTexture.getNormalPicId();
        String path = "";
        String normalPicPath = "";
        BaseBrand baseBrand = null;
        Integer textureBallFileId = groupProductTexture.getTextureBallFileId();
        String materialPath = "";
        String brandName = "";
        if (textureBallFileId != null && textureBallFileId.intValue() > 0) {
            ResModel resModel = this.getResModel(textureBallFileId);
            if (resModel != null) {
                materialPath = resModel.getModelPath();
            }
        }

        if (picId != null && picId.intValue() > 0) {
            ResPic resPic = this.getResPic(picId);
            if (resPic != null) {
                //材质显示用缩略图--原图加载太慢 add by: xiaoxc-20180906
                Integer smallPicId = Utils.getSmallPicId(resPic, "ios");
                if (null != smallPicId) {
                    ResPic resSmallPic = this.getResPic(smallPicId);
                    path = resSmallPic != null ? resSmallPic.getPicPath() : resPic.getPicPath();
                } else {
                    path = resPic.getPicPath();
                }
            }
        }
        if (normalPicId != null && normalPicId.intValue() > 0) {
            ResPic resPic = this.getResPic(normalPicId);
            if (resPic != null) {
                normalPicPath = /*Utils.getValue("app.resources.url", "") + */resPic.getPicPath();
                normalPicPath = Utils.dealWithPath(normalPicPath, "linux");
            }
        }
        /*添加品牌id与品牌名称*/
        if (null != groupProductTexture && null != groupProductTexture.getBrandId()) {
            baseBrand = this.getBaseBrand(groupProductTexture.getBrandId());
            if (null != baseBrand) {
                brandName = baseBrand.getBrandName();
            }
        }
        return new SplitTextureDTO().new ResTextureDTO(groupProductTexture.getId(), path,
                groupProductTexture.getTextureAttrValue(), groupProductTexture.getFileHeight(),
                groupProductTexture.getFileWidth(), groupProductTexture.getLaymodes(), materialPath,
                groupProductTexture.getNormalParam(), normalPicPath, groupProductTexture.getBrandId(),
                brandName, groupProductTexture.getTextureCode());
    }


    /**
     * 获取模型资源详情
     *
     * @param id
     * @return ResModel
     */
    @Override
    public ResModel getResModel(Integer id) {
        return resModelMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取图片资源详情
     *
     * @param id
     * @return ResPic
     */
    @Override
    public ResPic getResPic(Integer id) {
        return resPicMapper.selectByPrimaryKey(id);
    }


    /**
     * 获取数据详情
     *
     * @param id
     * @return BaseBrand
     */
    @Override
    public BaseBrand getBaseBrand(Integer id) {
        return baseBrandMapper.selectByPrimaryKey(id);
    }

    /**
     * getPropertyMap
     * <p>
     * 通过产品id  获取属性值 的map   map的结构（ 键是product_attribute表中attribute_key  值是product_props表中的prop_value）
     *
     * @param productId
     * @return
     */
    @Override
    public Map<String, String> getPropertyMap_old(Integer productId) {
        Map<String, String> map = new HashMap<String, String>();

        // List<ProductAttributePo> productAttributePos = productAttributeMetaDataStorage.getByProductId(productId);
        // if (productAttributePos != null && productAttributePos.size() > 0) {
        //     for (ProductAttributePo productAttributePo : productAttributePos) {
        //         if (null != productAttributePo) {
        //
        //             String productPropsParentCode = productAttributePo.getAttributeCode();
        //             String propValue = productAttributePo.getAttributeValue();
        //             String propCode = productAttributePo.getSonAttributeCode();
        //
        //             if (StringUtils.isNotBlank(productPropsParentCode)) {
        //                 map.put(productPropsParentCode, propValue);
        //                 //如果是结构产品标志 传Code值
        //                 if ("structureSign".equals(productPropsParentCode)) {
        //                     map.put(productPropsParentCode, propCode);
        //                 }
        //             }
        //         }
        //     }
        // }

        return map;
    }

    @Override
    public Map<String, String> getRulesSecondaryList(Integer productId, String productCode, String productTypeCode,
                                                     String productSmallTypeCode, Map<String, String> productAttributeMap) {
        Map<String, String> rulesMap = new HashMap<>();
        List<DesignRules> rulesList = new ArrayList<>();
        // 规则类型.由顺序决定优先级。下标越小优先级越高
        // 规则级别.由顺序决定优先级。下标越小优先级越高
        String[] rulesLevelArr = new String[]{"productLevel", "attributeLevel", "smallLevel", "bigLevel"};
        // 按优先级来查询规则。只返回优先级最高的规则
        DesignRules newDesignRules = new DesignRules();

        for (String rulesLevel : rulesLevelArr) {
            newDesignRules.setRulesLevel(rulesLevel);
            // 产品级别
            if ("productLevel".equals(rulesLevel)) {
                if (productId == null) {
                    continue;
                }
                newDesignRules.setRulesMainValue(StringUtils.trim(productCode));

                rulesList = designRulesMetaDataStorage.getByLevelAndMainValue(newDesignRules);
                // 如果查到有规则配置则直接返回
                if (rulesList != null && rulesList.size() > 0) {
                    for (DesignRules rules : rulesList) {
                        if (StringUtils.isBlank(rulesMap.get(rules.getRulesSign()))) {
                            rulesMap.put(rules.getRulesSign(), rules.getRulesSecondaryValues());
                        }
                    }
                    continue;
                }
            }
            //属性级别
            if ("attributeLevel".equals(rulesLevel)) {
                boolean flag = true;
                if (StringUtils.isBlank(productSmallTypeCode)) {
                    continue;
                }
                if (productAttributeMap == null || productAttributeMap.size() == 0) {
                    continue;
                }

                newDesignRules.setRulesMainObj(productSmallTypeCode);
                rulesList = designRulesMetaDataStorage.getByLevelAndMainObj(newDesignRules);

                // 如果查到有规则配置则直接返回
                if (rulesList != null && rulesList.size() > 0) {
                    Map<String, String> rulesProductAttributeMap;
                    for (DesignRules rules : rulesList) {
                        if (StringUtils.isNotBlank(rules.getRulesMainValue())) {

                            JSONObject json = JSONObject.fromObject(rules.getRulesMainValue());
                            if (json != null) {
                                // 规则中配置的属性
                                rulesProductAttributeMap = new HashMap<>();
                                for (String jsonKey : (Set<String>) json.keySet()) {
                                    if (StringUtils.isNotBlank(jsonKey)) {
                                        rulesProductAttributeMap.put(jsonKey, json.getString(jsonKey));
                                    }
                                }
                                int count = 0;// 记录一致属性的数量
                                for (String key : rulesProductAttributeMap.keySet()) {
                                    if (productAttributeMap.containsKey(key) && rulesProductAttributeMap.get(key).equals(productAttributeMap.get(key))) {
                                        count++;
                                    } else {
                                        continue;
                                    }
                                }
                                //产品属性等于或包含则匹配属性
                                if (rulesProductAttributeMap.size() == count) {
                                    rulesMap.put(rules.getRulesSign(), rules.getRulesSecondaryValues());
                                }
                                // 比较产品本身属性和规则中的属性是否一致
                            }
                        }
                    }
                    continue;
                }
            }
            // 小类级别
            if ("smallLevel".equals(rulesLevel)) {
                if (null == productId) {
                    continue;
                }
                newDesignRules.setRulesMainValue(productSmallTypeCode);
                rulesList = designRulesMetaDataStorage.getByLevelAndMainValue(newDesignRules);

                // 如果查到有规则配置则直接返回
                if (rulesList != null && rulesList.size() > 0) {
                    for (DesignRules rules : rulesList) {
                        if (StringUtils.isBlank(rulesMap.get(rules.getRulesSign()))) {
                            rulesMap.put(rules.getRulesSign(), rules.getRulesSecondaryValues());
                        }
                    }
                    continue;
                }
            }
            // 大类级别
            if ("bigLevel".equals(rulesLevel)) {
                if (null == productId) {
                    continue;
                }
                newDesignRules.setRulesMainValue(productTypeCode);
                rulesList = designRulesMetaDataStorage.getByLevelAndMainValue(newDesignRules);

                // 如果查到有规则配置则直接返回
                if (rulesList != null && rulesList.size() > 0) {
                    for (DesignRules rules : rulesList) {
                        if (StringUtils.isBlank(rulesMap.get(rules.getRulesSign()))) {
                            rulesMap.put(rules.getRulesSign(), rules.getRulesSecondaryValues());
                        }
                    }
                    continue;
                }
            }

        }
        return rulesMap;
    }


}
