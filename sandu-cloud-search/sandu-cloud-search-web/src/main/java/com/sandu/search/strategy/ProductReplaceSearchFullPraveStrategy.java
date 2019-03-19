package com.sandu.search.strategy;

import com.sandu.search.common.constant.ProductAttribute;
import com.sandu.search.common.constant.ProductAttributePropValue;
import com.sandu.search.common.constant.ProductSmallTypeValue;
import com.sandu.search.common.constant.ProductTypeValue;
import com.sandu.search.entity.elasticsearch.dco.ValueOffset;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * 产品替换搜索全铺长度策略
 *
 * @date 2018/3/22
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
public class ProductReplaceSearchFullPraveStrategy {

    private final static String CLASS_LOG_PREFIX = "产品替换搜索全铺长度策略:";

    /**
     * 适配白膜全铺长度
     *
     * @param productTypeValue          产品大类
     * @param productTypeSmallValue     产品小类
     * @param productFullPaveLength     产品全铺长度
     * @return
     */
    public static ValueOffset adaptationFullPraveLength(Integer productTypeValue, Integer productTypeSmallValue, int productFullPaveLength) {

        ValueOffset valueOffset = null;

        switch (productTypeValue) {
            //墙面
            case ProductTypeValue.PRODUCT_TYPE_VALUE_WALL:
                switch (productTypeSmallValue) {
                    //形象墙-背景墙-餐厅背景墙-电视背景墙-沙发背景墙-床头背景墙-定制装饰柜-定制储物柜-定制鞋柜-定制酒柜-定制书柜-定制电视柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_IMAGE_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_IMAGE_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BACKGROUND_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BACKGROUND_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DINING_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DINING_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TV_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TV_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SOFA_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SOFA_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BEDSIDE_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BEDSIDE_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_TV_CABINET_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_BOOK_CABINET_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_LIQUOR_CABINET_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SHOES_CABINET_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_DECORATION_CABINET_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SAVE_CABINET_WALL:
                        //左右偏移10cm
                        valueOffset = new ValueOffset(
                                productFullPaveLength,
                                10,
                                ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE,
                                ValueOffset.ONLY_INCLUDE_UPPER
                        );
                        break;
                    //门框-门框_原门框
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOOR_FRAME_WALL:
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DOOR_FRAME_WALL:
                        //左右偏移10cm
                        valueOffset = new ValueOffset(
                                productFullPaveLength,
                                10,
                                ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                        );
                        break;
                    default:
                        log.info(CLASS_LOG_PREFIX + "匹配墙面白模全铺长度完成，未找到对应小类:productSmallTypeValue:{}.", productTypeSmallValue);
                }
                break;
            //家纺
            case ProductTypeValue.PRODUCT_TYPE_VALUE_HOME_TEXTILES:
                //窗帘-定制窗帘
                if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CURTAIN_HOME_TEXTILES
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CURTAIN_HOME_TEXTILES
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CURTAIN_HOME_TEXTILES
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_CURTAIN_HOME_TEXTILES) {
                    //左右偏移20cm
                    valueOffset = new ValueOffset(
                            productFullPaveLength,
                            20,
                            ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                    );
                } else {
                    log.info(CLASS_LOG_PREFIX + "匹配家纺白模全铺长度完成，未找到对应小类:productSmallTypeValue:{}.", productTypeSmallValue);
                }
                break;
            //柜子
            case ProductTypeValue.PRODUCT_TYPE_VALUE_CABINET:
                switch (productTypeSmallValue) {
                    //衣柜_定制衣柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET:
                        //衣柜_原定制衣柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_CLOTHES_CABINET:
                        //左右偏移10cm
                        valueOffset = new ValueOffset(
                                productFullPaveLength,
                                10,
                                ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                        );
                        break;
                    default:
                        log.info(CLASS_LOG_PREFIX + "匹配柜子白模全铺长度完成，未找到对应小类:productSmallTypeValue:{}.", productTypeSmallValue);
                }
                break;
            //卫浴
            case ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM:
                //定制浴室柜
                if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_CABINET_BATHROOM) {
                    //左右偏移5cm
                    valueOffset = new ValueOffset(
                            productFullPaveLength,
                            5,
                            ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                    );
                    //一字型淋浴房
                } else if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ONE_SHOWER_BATHROOM
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_ONE_SHOWER_BATHROOM) {
                    //左右偏移10cm
                    valueOffset = new ValueOffset(
                            productFullPaveLength,
                            10,
                            ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                    );
                    //定制浴室镜
                } else if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_MIRROR_BATHROOM) {
                    //左右偏移5cm
                    valueOffset = new ValueOffset(
                            productFullPaveLength,
                            5,
                            ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                    );
                //原镜柜
                } else if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MIRROR_CABINET_BATHROOM) {
                    //左右偏移5cm
                    valueOffset = new ValueOffset(
                            productFullPaveLength,
                            5,
                            ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                    );
                } else {
                    log.info(CLASS_LOG_PREFIX + "匹配卫浴白模全铺长度完成，未找到对应小类:productSmallTypeValue:{}.", productTypeSmallValue);
                }
                break;
            //厨房地柜
            case ProductTypeValue.PRODUCT_TYPE_VALUE_KITCHEN_CABINETS:
                switch (productTypeSmallValue) {
                    //抽屉柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRAWER_KITCHEN_GROUND:
                        //原抽屉柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DRAWER_KITCHEN_GROUND:
                        //门地柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOOR_KITCHEN_GROUND:
                        //原门地柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DOOR_KITCHEN_GROUND:
                        //其他柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_KITCHEN_GROUND:
                        //原其他柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_KITCHEN_GROUND:
                        //靠左台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LEFT_KITCHEN_GROUND:
                        //原靠左台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_LEFT_KITCHEN_GROUND:
                        //靠右台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_RIGHT_KITCHEN_GROUND:
                        //原靠右台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_RIGHT_KITCHEN_GROUND:
                        //中间台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MIDDLE_KITCHEN_GROUND:
                        //原中间台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MIDDLE_KITCHEN_GROUND:
                        //左右偏移5cm
                        valueOffset = new ValueOffset(
                                productFullPaveLength,
                                5,
                                ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                        );
                        break;
                    default:
                        log.info(CLASS_LOG_PREFIX + "匹配厨房地柜白模全铺长度完成，未找到对应小类:productSmallTypeValue:{}.", productTypeSmallValue);
                }
                break;
            //厨房吊柜
            case ProductTypeValue.PRODUCT_TYPE_VALUE_KITCHEN_HANGING_CABINET:
                switch (productTypeSmallValue) {
                    //翻门吊柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CONVERT_DOOR_KITCHEN_CUPBOARD:
                        //原翻门吊柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CONVERT_DOOR_KITCHEN_CUPBOARD:
                        //门吊柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOOR_KITCHEN_CUPBOARD:
                        //原门吊柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DOOR_KITCHEN_CUPBOARD:
                        //其他吊柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_KITCHEN_CUPBOARD:
                        //原其他吊柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_KITCHEN_CUPBOARD:
                        //左右偏移5cm
                        valueOffset = new ValueOffset(
                                productFullPaveLength,
                                5,
                                ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                        );
                        break;
                    default:
                        log.info(CLASS_LOG_PREFIX + "匹配厨房吊柜白模全铺长度完成，未找到对应小类:productSmallTypeValue:{}.", productTypeSmallValue);
                }
                break;
            //门
            case ProductTypeValue.PRODUCT_TYPE_VALUE_DOOR:
                //入户门-房间门-卫生间门-厨房门-阳台门
                if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ENTRY_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_ENTRY_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ROOM_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_ROOM_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOILET_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TOILET_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_KITCHEN_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_KITCHEN_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BALCONY_DOOR
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BALCONY_DOOR) {
                    //左右偏移10
                    valueOffset = new ValueOffset(
                            productFullPaveLength,
                            10,
                            ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE
                    );
                }
                break;
            default:
                log.info(CLASS_LOG_PREFIX + "匹配白模全铺长度完成，未找到对应大小类:productTypeValue:{}, productSmallTypeValue:{}.",
                        productTypeValue,
                        productTypeSmallValue);
        }

        return valueOffset;
    }


    /**
     * 根据产品的属性适配白膜全铺长度
     * @param fileterAttributeMap 产品过滤属性集合
     * @param productTypeValue 产品大类
     * @param productTypeSmallValue 产品小类
     * @param productFullPaveLength 产品全铺长度
     * @return
     */
    public static ValueOffset  adaptationFullPraveLengthByProductAttribute(Integer productTypeValue, Integer productTypeSmallValue, int productFullPaveLength,Map<String, String> fileterAttributeMap){
        if(fileterAttributeMap == null || fileterAttributeMap.size() < 1){
            return null;
        }
        ValueOffset valueOffset = null;
        switch (productTypeValue){
            case ProductTypeValue.PRODUCT_TYPE_VALUE_DOOR:
                //类别属性信息
                if(fileterAttributeMap.containsKey(ProductAttribute.PRODUCTE_PROP_CATEGORY_TYPE)){
                    //推拉门属性
                    String attributeValue = fileterAttributeMap.get(ProductAttribute.PRODUCTE_PROP_CATEGORY_TYPE);
                    if(Objects.equals(attributeValue, ProductAttributePropValue.DOOR_TYPE_SLIDING_PROP_VALUE)){
                        if(productFullPaveLength >= 210){
                            //白膜长度大于/等于210cm
                            valueOffset = new ValueOffset(productFullPaveLength ,20, ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE);
                        }else{
                            valueOffset = new ValueOffset(productFullPaveLength ,10, ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return valueOffset;
    }
}