package com.sandu.search.strategy;

import com.sandu.search.common.constant.ProductSmallTypeValue;
import com.sandu.search.common.constant.ProductTypeValue;
import com.sandu.search.entity.elasticsearch.dco.ValueRange;
import lombok.extern.slf4j.Slf4j;

/**
 * 产品替换搜索高度策略
 *
 * @date 2018/3/22
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
public class ProductReplaceSearchHeightStrategy {

    private final static String CLASS_LOG_PREFIX = "产品替换搜索高度策略:";

    public static ValueRange adaptationProductHeight(Integer productTypeValue, Integer productTypeSmallValue, int productWhiteMembraneHeight) {
        ValueRange valueRange = null;

        //产品分类区分
        switch (productTypeValue) {
            //墙面
            case ProductTypeValue.PRODUCT_TYPE_VALUE_WALL:
                //背景墙-形象墙-电视墙-沙发墙-床头墙-餐厅墙
                if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BACKGROUND_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BACKGROUND_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_IMAGE_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_IMAGE_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TV_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TV_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SOFA_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SOFA_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BEDSIDE_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BEDSIDE_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DINING_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DINING_WALL) {
                    //若高度0-40(含)则搜索0-40(含)范围,若高度40-52(含)则搜索40-52(含)范围,若高度大于52,则搜索52-260(含)的范围高度
                    if (0 < productWhiteMembraneHeight && productWhiteMembraneHeight <= 40) {
                        valueRange = new ValueRange(
                                ValueRange.RANGE_TYPE_ONLY_END_EQUAL,
                                0,
                                40
                        );
                    } else if (40 < productWhiteMembraneHeight && productWhiteMembraneHeight <= 52) {
                        valueRange = new ValueRange(
                                ValueRange.RANGE_TYPE_ONLY_END_EQUAL,
                                40,
                                52
                        );
                    } else if (52 < productWhiteMembraneHeight && productWhiteMembraneHeight < 260) {
                        valueRange = new ValueRange(
                                ValueRange.RANGE_TYPE_ONLY_END_EQUAL,
                                52,
                                260
                        );
                    } else {
                        log.info(CLASS_LOG_PREFIX + "高度规则匹配失败,未在匹配范围,当前高度:{}.", productWhiteMembraneHeight);
                    }
                //门框-窗框
                } else if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOOR_FRAME_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DOOR_FRAME_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WINDOW_FRAME_WALL
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WINDOW_FRAME_WALL) {
                    //按原高度过滤
                    valueRange = new ValueRange(
                            ValueRange.RANGE_TYPE_ALL_EQUAL,
                            productWhiteMembraneHeight,
                            productWhiteMembraneHeight
                    );
                }
                break;
            //家纺
            case ProductTypeValue.PRODUCT_TYPE_VALUE_HOME_TEXTILES:
                //窗帘-定制窗帘
                if (productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CURTAIN_HOME_TEXTILES
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CURTAIN_HOME_TEXTILES
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CURTAIN_HOME_TEXTILES
                        || productTypeSmallValue == ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_CURTAIN_HOME_TEXTILES) {
                    //按原高度过滤
                    valueRange = new ValueRange(
                            ValueRange.RANGE_TYPE_ALL_EQUAL,
                            productWhiteMembraneHeight,
                            productWhiteMembraneHeight
                    );
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
                    //按原高度过滤
                    valueRange = new ValueRange(
                            ValueRange.RANGE_TYPE_ALL_EQUAL,
                            productWhiteMembraneHeight,
                            productWhiteMembraneHeight
                    );
                }
                break;
            default:
                log.info(CLASS_LOG_PREFIX + "此产品大/小类无高度过滤,大类:{},小类:{}", productTypeValue, productTypeSmallValue);
        }

        return valueRange;
    }
}
