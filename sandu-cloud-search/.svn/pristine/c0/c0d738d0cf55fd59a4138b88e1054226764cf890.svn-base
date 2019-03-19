package com.sandu.search.strategy;

import com.sandu.search.common.constant.ProductSmallTypeValue;
import com.sandu.search.common.constant.ProductTypeValue;
import com.sandu.search.common.constant.SystemDictionaryType;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.SystemDictionaryPo;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品分类匹配策略
 *
 * @date 20180104
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ProductTypeMatchStrategy {

    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;

    private final static String CLASS_LOG_PREFIX = "产品分类匹配策略:";

    @Autowired
    public ProductTypeMatchStrategy(SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage) {
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
    }

    /**
     * 匹配产品小类规则
     *
     * @param productTypeValue      产品大类
     * @param productTypeSmallValue 产品小类
     * @return 产品大类/小类List
     */
    public List<Integer> matchProductSmallTypeRule(int productTypeValue, int productTypeSmallValue) {

        if (0 == productTypeValue || 0 == productTypeSmallValue) {
             return null;
        }

        //产品小类列表
        List<Integer> productSmallTypeList = new ArrayList<>();

        //产品大类
        switch (productTypeValue) {
            //天花
            case ProductTypeValue.PRODUCT_TYPE_VALUE_CEILING :
                //截面天花
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SECTION_CEILING);
                break;
            //地面
            case ProductTypeValue.PRODUCT_TYPE_VALUE_GROUND :
                switch (productTypeSmallValue) {
                    //地砖
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BRICK_GROUND :
                    //地板
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLOOR_GROUND:
                    //地毯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CARPET_GROUND:
                    //地面石材
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STONE_GROUND:
                    //地面_原地面(通用)
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_GROUND:
                    //原主地面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MAIN_GROUND:
                    //原过道地面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_PASSAGE_GROUND:
                    //原玄关地面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_HALLWAY_GROUND:
                    //原门槛地面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_THRESHOLD_GROUND:
                    //原窗台石
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WINDOW_STONE_GROUND:
                    //原阳台地面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BALCONY_GROUND:
                        //地板-地毯-地砖-地面石材-波打线
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BRICK_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLOOR_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CARPET_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STONE_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WAVE_LINE_GROUND);
                        break;
                    //踢脚线
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SKIRTING_LINE_GROUND:
                    //原踢脚线
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SKIRTING_LINE_GROUND:
                        //踢脚线-地砖-地面石材
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SKIRTING_LINE_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BRICK_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STONE_GROUND);
                        break;
                    //波打线
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WAVE_LINE_GROUND:
                    //原波打线
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WAVE_LINE_GROUND:
                        //波打线-地砖-地板-地面石材
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WAVE_LINE_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BRICK_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLOOR_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STONE_GROUND);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配地面产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //墙面
            case ProductTypeValue.PRODUCT_TYPE_VALUE_WALL:
                //
                switch (productTypeSmallValue) {
                    //墙纸
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WALLPAPER_WALL:
                    //墙砖
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BRICK_WALL:
                    //墙面石材
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STONE_SPACE_WALL:
                    //涂料
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COATING_WALL:
                    //墙面_原墙面(墙体)
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SPACE_WALL:
                        //墙纸-墙砖-墙面石材-涂料
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WALLPAPER_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BRICK_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STONE_SPACE_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COATING_WALL);
                        break;
                    //腰线
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BELT_COURSE_WALL:
                    //原腰线
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BELT_COURSE_WALL:
                        //腰线-墙砖-墙面石材
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BELT_COURSE_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BRICK_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STONE_SPACE_WALL);
                        break;
                    //形象墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_IMAGE_WALL:
                    //形象墙_原形象墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_IMAGE_WALL:
                    //背景墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BACKGROUND_WALL:
                    //背景墙_原背景墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BACKGROUND_WALL:
                    //餐厅墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DINING_WALL:
                    //餐厅墙_原餐厅墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DINING_WALL:
                    //电视墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TV_WALL:
                    //电视墙_原电视墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TV_WALL:
                    //沙发墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SOFA_WALL:
                    //沙发墙_原沙发墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SOFA_WALL:
                    //床头墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BEDSIDE_WALL:
                    //床头墙_原床头墙
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BEDSIDE_WALL:
                    //定制装饰柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_DECORATION_CABINET_WALL:
                    //定制储物柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SAVE_CABINET_WALL:
                    //定制鞋柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SHOES_CABINET_WALL:
                    //定制酒柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_LIQUOR_CABINET_WALL:
                    //定制书柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_BOOK_CABINET_WALL:
                    //定制电视柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_TV_CABINET_WALL:
                        //形象墙-背景墙-餐厅背景墙-电视背景墙-沙发背景墙-床头背景墙-定制装饰柜-定制储物柜-定制鞋柜-定制酒柜-定制书柜-定制电视柜
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_IMAGE_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BACKGROUND_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DINING_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TV_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SOFA_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BEDSIDE_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_TV_CABINET_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_BOOK_CABINET_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_LIQUOR_CABINET_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SHOES_CABINET_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_DECORATION_CABINET_WALL);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SAVE_CABINET_WALL);
                        break;
                    //门框
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOOR_FRAME_WALL:
                    //门框_原门框
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DOOR_FRAME_WALL:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOOR_FRAME_WALL);
                        break;
                    //窗框
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WINDOW_FRAME_WALL:
                    //窗框_原窗框
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WINDOW_FRAME_WALL:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WINDOW_FRAME_WALL);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配墙产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //门
            case ProductTypeValue.PRODUCT_TYPE_VALUE_DOOR:
                switch (productTypeSmallValue) {
                    //入户门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ENTRY_DOOR:
                    //原入户门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_ENTRY_DOOR:
                    //房间门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ROOM_DOOR:
                    //原房间门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_ROOM_DOOR:
                    //厨房门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_KITCHEN_DOOR:
                    //原厨房门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_KITCHEN_DOOR:
                    //卫生间门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOILET_DOOR:
                    //原卫生间门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TOILET_DOOR:
                    //阳台门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BALCONY_DOOR:
                    //原阳台门
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BALCONY_DOOR:
                        //入户门-房间门-厨房门-卫生间门-阳台门
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ENTRY_DOOR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ROOM_DOOR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_KITCHEN_DOOR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOILET_DOOR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BALCONY_DOOR);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配门产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //柜类
            case ProductTypeValue.PRODUCT_TYPE_VALUE_CABINET:
                switch (productTypeSmallValue) {
                    //博古架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ANTIQUE_CABINET:
                    //博古架_原博古架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_ANTOQUE_CABINET:
                    //餐柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MEAL_CABINET:
                    //餐柜_原餐柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MEAL_CABINET:
                    //床头柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BEDSIDE_CABINET:
                    //床头柜_原床头柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BEDSIDE_CABINET:
                    //电视柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TV_CABINET:
                    //电视柜_原电视柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TV_CABINET:
                    //定制柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET:
                    //原定制柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_CABINET:
                    //定制家具
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FURNITURE_CABINET:
                    //原定制家具
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FURNITURE_CABINET:
                    //衣柜_定制衣柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET:
                    //衣柜_原定制衣柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_CLOTHES_CABINET:
                    //斗柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRAWERS_CABINET:
                    //斗柜_原斗柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DRAWERS_CABINET:
                    //酒柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LIQUOR_CABINET:
                    //酒柜_原酒柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_LIQUOR_CABINET:
                    //屏风
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SCREEN_CABINET:
                    //屏风_原屏风
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SCREEN_CABINET:
                    //书柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_CABINET:
                    //书柜_原书柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BOOK_CABINET:
                    //鞋柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SHOE_CABINET:
                    //鞋柜_原鞋柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SHOE_CABINET:
                    //衣柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CLOTHES_CABINET:
                    //衣柜_原衣柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CLOTHES_CABINET:
                    //装饰柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DECORATION_CABINET:
                    //装饰柜_原装饰柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DECORATION_CABINET:
                    //储物柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SAVE_CABINET:
                    //储物柜_原储物柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SAVE_CABINET:
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_CABINET:
                    //其他_原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_CABINET:
                    //柜子_原柜子
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CABINET:
                        //博古架-餐柜-床头柜-电视柜-定制柜-定制家具-定制衣柜-斗柜-酒柜-屏风-书柜-鞋柜-衣柜-装饰柜-储物柜-其他
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ANTIQUE_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MEAL_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BEDSIDE_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TV_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FURNITURE_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRAWERS_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LIQUOR_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SCREEN_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SHOE_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CLOTHES_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DECORATION_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SAVE_CABINET);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_CABINET);
                        break;
                    //搁板/壁架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WALL_CABINET:
                    //搁板_原搁板/壁架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WALL_CABINET:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WALL_CABINET);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配柜类产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //几类
            case ProductTypeValue.PRODUCT_TYPE_VALUE_TEA_TABLE:
                switch (productTypeSmallValue) {
                    //边几
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SIDE_SMALL_TABLE:
                    //边几_原边几
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SIDE_SMALL_TABLE:
                    //茶几
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TEA_SMALL_TABLE:
                    //茶几_原茶几
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TEA_SMALL_TABLE:
                    //花架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLOWER_SMALL_TABLE:
                    //花架_原花架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FLOWER_SMALL_TABLE:
                    //书架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_SMALL_TABLE:
                    //书架_原书架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BOOK_SMALL_TABLE:
                    //箱子
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOX_SMALL_TABLE:
                    //箱子_原箱子
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BOX_SMALL_TABLE:
                    //衣帽架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CAP_SMALL_TABLE:
                    //衣帽架_原衣帽架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CAP_SMALL_TABLE:
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_SMALL_TABLE:
                    //其他_原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_SMALL_TABLE:
                        //边几-茶几-花架-书架-箱子-衣帽架-其他
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SIDE_SMALL_TABLE);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TEA_SMALL_TABLE);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLOWER_SMALL_TABLE);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_SMALL_TABLE);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOX_SMALL_TABLE);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CAP_SMALL_TABLE);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_SMALL_TABLE);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配几类产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //沙发
            case ProductTypeValue.PRODUCT_TYPE_VALUE_SOFA:
                //单人沙发-三人沙发-U形沙发-组合沙发-红木组合沙发-脚踏沙发-双人沙发-其他沙发
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SINGLE_SOFA);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_THIRE_SOFA);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_U_SOFA);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_GROUP_SOFA);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_RED_GROUP_SOFA);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FOOT_SOFA);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOUBLE_SOFA);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_SOFA);
                break;
            //饰品
            case ProductTypeValue.PRODUCT_TYPE_VALUE_ACCESSORIES:
                switch (productTypeSmallValue) {
                    //摆件
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SMALL_ACCESSORIES:
                    //原摆件
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SMALL_ACCESSORIES:
                    //花艺
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLOWER_ACCESSORIES:
                    //原花艺
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FLOWER_ACCESSORIES:
                    //生活用品
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LIFE_ACCESSORIES:
                    //原生活用品
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_LIFE_ACCESSORIES:
                    //书籍
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_ACCESSORIES:
                    //原书籍
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_NOOK_ACCESSORIES:
                        //摆件-花艺-生活用品-书籍-其他
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SMALL_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLOWER_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LIFE_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_ACCESSORIES);
                        break;
                    //挂件
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PENDANT_ACCESSORIES:
                    //原挂件
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_PENDANT_ACCESSORIES:
                    //挂件_原挂件
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PENDANT_ORIGIN_PENDANT_ACCESSORIES:
                    //创作画
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CREATION_PICTURE_ACCESSORIES:
                    //原创作画
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CREATION_PICTURE_ACCESSORIES:
                    //镜子
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MIRROR_ACCESSORIES:
                    //镜子_原镜子
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MIRROR_ACCESSORIES:
                    //装饰画
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PICTURE_ACCESSORIES:
                    //装饰画_原装饰画
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_PICTURE_ACCESSORIES:
                        //挂件-创作画-镜子-装饰画-其他
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PENDANT_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CREATION_PICTURE_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MIRROR_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PICTURE_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_ACCESSORIES);
                        break;
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_ACCESSORIES:
                    //其他_原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_ACCESSORIES:
                        //摆件-花艺-生活用品-书籍-挂件-创作画-镜子-装饰画-其他
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SMALL_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLOWER_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LIFE_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PENDANT_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CREATION_PICTURE_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MIRROR_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PICTURE_ACCESSORIES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_ACCESSORIES);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配饰品产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //椅子
            case ProductTypeValue.PRODUCT_TYPE_VALUE_CHAIR:
                switch (productTypeSmallValue) {
                    //吧台凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BAR_CHAIR:
                    //台凳_原吧台凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BAR_CHAIR:
                    //餐椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MEAL_CHAIR:
                    //餐椅_原餐椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MEAL_CHAIR:
                    //床尾凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TAILSTOCK_CHAIR:
                    //床尾凳_原床尾凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TAILSTOCK_CHAIR:
                    //鼓凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRUM_CHAIR:
                    //原鼓凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DRUM_CHAIR:
                    //贵妃椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PRINCE_CHAIR:
                    //贵妃椅_原贵妃椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_PRINCE_CHAIR:
                    //情侣椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LOVERS_CHAIR:
                    //情侣椅_原情侣椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_LOVERS_CHAIR:
                    //圈椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CIRCLE_CHAIR:
                    //圈椅_原圈椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CIRCLE_CHAIR:
                    //梳妆凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRESS_CHAIR:
                    //梳妆凳_原梳妆凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DRESS_CHAIR:
                    //书凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_CHAIR:
                    //书凳_原书凳
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BOOK_CHAIR:
                    //休闲椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_RELAXTION_CHAIR:
                    //休闲椅_原休闲椅
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_RELAXTION_CHAIR:
                    //椅子_原椅子
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CHAIR:
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_CHAIR:
                        //吧台椅-餐椅-床尾椅-鼓凳-贵妃椅-情侣椅-圈椅-梳妆椅-书凳-休闲椅-其他
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BAR_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MEAL_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TAILSTOCK_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRUM_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PRINCE_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LOVERS_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CIRCLE_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRESS_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_RELAXTION_CHAIR);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_CHAIR);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配椅子产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //桌子
            case ProductTypeValue.PRODUCT_TYPE_VALUE_DESK:
                switch (productTypeSmallValue) {
                    //餐桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MEAL_DESK:
                    //餐桌_原餐桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MEAL_DESK:
                    //书桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_DESK:
                    //书桌_原书桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BOOK_DESK:
                    //梳妆台
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRESS_DESK:
                    //梳妆台_原梳妆台
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DRESS_DESK:
                    //展示桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SHOW_DESK:
                    //展示桌_原展示桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SHOW_DESK:
                    //吧台
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BAR_DESK:
                    //吧台_原吧台
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BAR_DESK:
                    //玄关
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HALLWAY_DESK:
                    //玄关_原玄关
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_HALLWAY_DESK:
                    //案几
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FEW_DESK:
                    //原案几
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FEW_DESK:
                    //棋桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CHESS_DESK:
                    //棋桌_原棋桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CHESS_DESK:
                    //茶桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TEA_DESK:
                    //原茶桌
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TEA_DESK:
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_DESK:
                    //其他_原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_DESK:
                        //餐桌-书桌-梳妆台-展示桌-吧台-玄关-案几-棋桌-茶桌-其他
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MEAL_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOOK_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRESS_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SHOW_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BAR_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HALLWAY_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FEW_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CHESS_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TEA_DESK);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_DESK);
                        break;
                    //桌子_原桌子
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DESK:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DESK);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配桌子产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //厨房电器
            case ProductTypeValue.PRODUCT_TYPE_VALUE_KITCHEN_ELECTRIC_EQUIPMENT:
                switch (productTypeSmallValue) {
                    //灶具
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STOVE_KITCHEN_DEVICE:
                    //灶具_原灶具
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_STOVE_KITCHEN_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STOVE_KITCHEN_DEVICE);
                        break;
                    //油烟机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SMOKE_EXHAUST_KITCHEN_DEVICE:
                    //油烟机_原油烟机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SMOKE_EXHAUST_KITCHEN_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SMOKE_EXHAUST_KITCHEN_DEVICE);
                        break;
                    //消毒柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DISINFECTION_CABINET_KITCHEN_DEVICE:
                    //消毒柜_原消毒柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DISINFECTION_CABINET_KITCHEN_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DISINFECTION_CABINET_KITCHEN_DEVICE);
                        break;
                    //微波炉
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MICROWAVE_OVEN_KITCHEN_DEVICE:
                    //微波炉_原微波炉
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MICROWAVE_OVEN_KITCHEN_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MICROWAVE_OVEN_KITCHEN_DEVICE);
                        break;
                    //烤箱
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OVEN_KITCHEN_DEVICE:
                    //烤箱_原烤箱
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OVEN_KITCHEN_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OVEN_KITCHEN_DEVICE);
                        break;
                    //洗碗机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WASH_MACHINE_KITCHEN_DEVICE:
                    //洗碗机_原洗碗机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WASH_MACHINE_KITCHEN_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WASH_MACHINE_KITCHEN_DEVICE);
                        break;
                    //热水器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WATER_HEATER_KITCHEN_DEVICE:
                    //热水器_原热水器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WATER_HEATER_KITCHEN_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WATER_HEATER_KITCHEN_DEVICE);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配厨房电器产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //小家电
            case ProductTypeValue.PRODUCT_TYPE_VALUE_SMALL_HOME_APPLIANCES:
                switch (productTypeSmallValue) {
                    //加湿器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HUMIDIFIER_SMALL_DEVICE:
                    //加湿器_原加湿器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_HUMIDIFIER_SMALL_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HUMIDIFIER_SMALL_DEVICE);
                        break;
                    //电话机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PHONE_SMALL_DEVICE:
                    //电话机_原电话机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_PHONE_SMALL_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PHONE_SMALL_DEVICE);
                        break;
                    //净水机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WATER_FILTER_SMALL_DEVICE:
                    //净水机_原净水机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WATER_FILTER_SMALL_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WATER_FILTER_SMALL_DEVICE);
                        break;
                    //饮水机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WATER_DISPENSER_SMALL_DEVICE:
                    //饮水机_原饮水机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WATER_DISPENSER_SMALL_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WATER_DISPENSER_SMALL_DEVICE);
                        break;
                    //空气净化机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_AIR_CLEANER_SMALL_DEVICE:
                    //净化机_原空气净化机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_AIR_CLEANER_SMALL_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_AIR_CLEANER_SMALL_DEVICE);
                        break;
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_SMALL_DEVICE:
                    //原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_SMALL_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_SMALL_DEVICE);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配小家电产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //大家电(电器)
            case ProductTypeValue.PRODUCT_TYPE_VALUE_ELECTRIC_EQUIPMENT:
                switch (productTypeSmallValue) {
                    //电视
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TV_BIG_DEVICE:
                    //电视_原电视
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TV_BIG_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TV_BIG_DEVICE);
                        break;
                    //冰箱
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_REFRIGERATOR_BIG_DEVICE:
                    //冰箱_原冰箱
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_REFRIGERATOR_BIG_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_REFRIGERATOR_BIG_DEVICE);
                        break;
                    //空调
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_AIR_CONDITIONER_BIG_DEVICE:
                    //空调_原空调
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_AIR_CONDITIONER_BIG_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_AIR_CONDITIONER_BIG_DEVICE);
                        break;
                    //洗衣机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WASHER_BIG_DEVICE:
                    //洗衣机_原洗衣机
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WASHER_BIG_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WASHER_BIG_DEVICE);
                        break;
                    //家庭影院
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FAMILY_CINEMA_BIG_DEVICE:
                    //家庭影院_原家庭影院
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FAMILY_CINEMA_BIG_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FAMILY_CINEMA_BIG_DEVICE);
                        break;
                    //电脑
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COMPUTER_BIG_DEVICE:
                    //电脑_原电脑
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_COMPUTER_BIG_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COMPUTER_BIG_DEVICE);
                        break;
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_BIG_DEVICE:
                    //其他_原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_BIG_DEVICE:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_BIG_DEVICE);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配大家电(电器)产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //家纺
            case ProductTypeValue.PRODUCT_TYPE_VALUE_HOME_TEXTILES:
                switch (productTypeSmallValue) {
                    //窗帘
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CURTAIN_HOME_TEXTILES:
                    //窗帘_原窗帘
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CURTAIN_HOME_TEXTILES:
                    //窗帘_定制窗帘
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CURTAIN_HOME_TEXTILES:
                    //窗帘_原定制窗帘
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_CURTAIN_HOME_TEXTILES:
                        //窗帘-定制窗帘
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CURTAIN_HOME_TEXTILES);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CURTAIN_HOME_TEXTILES);
                        break;
                    //地毯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CARPET_HOME_TEXTILES:
                    //地毯_原地毯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CARPET_HOME_TEXTILES:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CARPET_HOME_TEXTILES);
                        break;
                    //抱枕
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOLSTER_HOME_TEXTILES:
                    //抱枕_原抱枕
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BOLSTER_HOME_TEXTILES:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOLSTER_HOME_TEXTILES);
                        break;
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_HOME_TEXTILES:
                    //其他_原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_HOME_TEXTILES:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_HOME_TEXTILES);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配家纺产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //床垫
            case ProductTypeValue.PRODUCT_TYPE_VALUE_MATTRESS:
                //床垫
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MATTRESS);
                break;
            //床品
            case ProductTypeValue.PRODUCT_TYPE_VALUE_BEDDING:
                //床上用品
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BED_PRODUCT);
                break;
            //灯具
            case ProductTypeValue.PRODUCT_TYPE_VALUE_LIGHT:
                switch (productTypeSmallValue) {
                    //吸顶灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CEILING_LIGHT:
                    //吸顶灯_原吸顶灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CEILING_LIGHT:
                    //吊灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HANG_LIGHT:
                    //吊灯_原吊灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_HANG_LIGHT:
                    //射灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SPOT_LIGHT:
                    //射灯_原射灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SPOT_LIGHT:
                    //筒灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOWN_LIGHT:
                    //筒灯_原筒灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DOWN_LIGHT:
                        //吸顶灯-吊灯-射灯-筒灯
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CEILING_LIGHT);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HANG_LIGHT);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SPOT_LIGHT);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOWN_LIGHT);
                        break;
                    //台灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STAGE_LIGHT:
                    //台灯_原台灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_STAGE_LIGHT:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STAGE_LIGHT);
                        break;
                    //壁灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WALL_LIGHT:
                    //壁灯_原壁灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WALL_LIGHT:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WALL_LIGHT);
                        break;
                    //落地灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_GROUND_LIGHT:
                    //落地灯_原落地灯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_GROUND_LIGHT:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_GROUND_LIGHT);
                        break;
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_LIGHT:
                    //其他_原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_LIGHT:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_LIGHT);
                        break;
                    //灯具_原灯具
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_LIGHT:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_LIGHT);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配灯具产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //卫浴
            case ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM:
                switch (productTypeSmallValue) {
                    //定制浴室柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM:
                    //定制浴室柜_原定制浴室柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_CABINET_BATHROOM:
                    //浴室柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CABINET_BATHROOM:
                    //浴室柜_原浴室柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CABINET_BATHROOM:
                        //定制浴室柜-浴室柜
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CABINET_BATHROOM);
                        break;
                    //浴室镜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SHOWER_MIRROR_BATHROOM:
                    //原浴室镜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SHOWER_MIRROR_BATHROOM:
                    //定制浴室镜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM:
                    //原定制浴室镜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_MIRROR_BATHROOM:
                    //镜柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MIRROR_CABINET_BATHROOM:
                    //原镜柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MIRROR_CABINET_BATHROOM:
                        //浴室镜-定制浴室镜-镜柜
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SHOWER_MIRROR_BATHROOM);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MIRROR_CABINET_BATHROOM);
                        break;
                    //浴缸
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BATHTUB_BATHROOM:
                    //浴缸_原浴缸
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BATHTUB_BATHROOM:
                        //浴缸
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BATHTUB_BATHROOM);
                        break;
                    //浴缸_嵌入式浴缸
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLUSH_BATHROOM:
                    //浴缸_原嵌入式浴缸
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FLUSH_BATHROOM:
                        //浴缸_嵌入式浴缸
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLUSH_BATHROOM);
                        break;
                    //面盆
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FACE_BATHROOM:
                    //面盆_原面盆
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FACE_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FACE_BATHROOM);
                        break;
                    //柱盆
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BASIN_BATHROOM:
                    //柱盆_原柱盆
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BASIN_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BASIN_BATHROOM);
                        break;
                    //浴室配件
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CONFIGURATION_BATHROOM:
                    //浴室配件_原浴室配件
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CONFIGURATION_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CONFIGURATION_BATHROOM);
                        break;
                    //浴霸
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HEATER_BATHROOM:
                    //浴霸_原浴霸
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_HEATER_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HEATER_BATHROOM);
                        break;
                    //座便器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOILET_BATHROOM:
                    //座便器_原座便器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TOILET_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOILET_BATHROOM);
                        break;
                    //蹲便器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SQUATTING_BATHROOM:
                    //蹲便器_原蹲便器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SQUATTING_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SQUATTING_BATHROOM);
                        break;
                    //淋浴房
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SHOWER_BATHROOM:
                    //淋浴房_原淋浴房
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SHOWER_BATHROOM:
                    //淋浴房_定制淋浴房
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SHOWER_BATHROOM:
                    //淋浴房_原定制淋浴房
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DIY_SHOWER_BATHROOM:
                        //淋浴房-淋浴房_定制淋浴房
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SHOWER_BATHROOM);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SHOWER_BATHROOM);
                        break;
                    //水龙头
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FAUCET_BATHROOM:
                    //水龙头_原水龙头
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FAUCET_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FAUCET_BATHROOM);
                        break;
                    //花洒
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SPRINKLER_BATHROOM:
                    //花洒_原花洒
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SPRINKLER_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SPRINKLER_BATHROOM);
                        break;
                    //一字型淋浴房
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ONE_SHOWER_BATHROOM:
                    //原一字型淋浴房
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_ONE_SHOWER_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ONE_SHOWER_BATHROOM);
                        break;
                    //其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_BATHROOM:
                    //其他_原其他
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_OTHER_BATHROOM:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_BATHROOM);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配卫浴产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //浴室配件
            case ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM_ACCESSORIES:
                switch (productTypeSmallValue) {
                    //挡水条
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BACK_WATER_BATHROOM_CONFIGURATION:
                    //原挡水条
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BACK_WATER_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BACK_WATER_BATHROOM_CONFIGURATION);
                        break;
                    //肥皂台
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SOAP_BATHROOM_CONFIGURATION:
                    //原肥皂台
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SOAP_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SOAP_BATHROOM_CONFIGURATION);
                        break;
                    //妇洗器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLUSH_BATHROOM_CONFIGURATION:
                    //原妇洗器
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FLUSH_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FLUSH_BATHROOM_CONFIGURATION);
                        break;
                    //挂衣钩
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PUT_CLOTHES_HOCK_BATHROOM_CONFIGURATION:
                    //原挂衣钩
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_PUT_CLOTHES_HOCK_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PUT_CLOTHES_HOCK_BATHROOM_CONFIGURATION);
                        break;
                    //马桶刷盒
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOILET_BRUSH_BATHROOM_CONFIGURATION:
                    //原马桶刷盒
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TOILET_BRUSH_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOILET_BRUSH_BATHROOM_CONFIGURATION);
                        break;
                    //毛巾架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOWEL_BATHROOM_CONFIGURATION:
                    //原毛巾架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TOWEL_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOWEL_BATHROOM_CONFIGURATION);
                        break;
                    //牙杯架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOOTH_CUP_BATHROOM_CONFIGURATION:
                    //原牙杯架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TOOTH_CUP_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOOTH_CUP_BATHROOM_CONFIGURATION);
                        break;
                    //牙刷杯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOOTHBRUSH_CUP_BATHROOM_CONFIGURATION:
                    //原牙刷杯
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TOOTHBRUSH_CUP_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TOOTHBRUSH_CUP_BATHROOM_CONFIGURATION);
                        break;
                    //衣钩
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CLOTHES_HOCK_BATHROOM_CONFIGURATION:
                    //原衣钩
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_CLOTHER_HOCK_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CLOTHES_HOCK_BATHROOM_CONFIGURATION);
                        break;
                    //浴缸水龙头
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BATHTUB_BATHROOM_CONFIGURATION:
                    //原浴缸水龙头
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BATHTUB_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BATHTUB_BATHROOM_CONFIGURATION);
                        break;
                    //浴巾架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BATH_TOWEL_BATHROOM_CONFIGURATION:
                    //原浴巾架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BATH_TOWEL_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BATH_TOWEL_BATHROOM_CONFIGURATION);
                        break;
                    //浴室柜水龙头
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FAUCET_BATHROOM_CONFIGURATION:
                    //原浴室柜水龙头
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FAUCET_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FAUCET_BATHROOM_CONFIGURATION);
                        break;
                    //纸筒架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PAPER_ROCK_BATHROOM_CONFIGURATION:
                    //原纸筒架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_PAPER_ROCK_BATHROOM_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_PAPER_ROCK_BATHROOM_CONFIGURATION);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配浴室配件产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
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
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DRAWER_KITCHEN_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOOR_KITCHEN_GROUND);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_KITCHEN_GROUND);
                        break;
                    //靠右台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_RIGHT_KITCHEN_GROUND:
                    //原靠右台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_RIGHT_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_RIGHT_KITCHEN_GROUND);
                        break;
                    //靠左台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LEFT_KITCHEN_GROUND:
                    //原靠左台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_LEFT_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_LEFT_KITCHEN_GROUND);
                        break;
                    //水盆柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BASIN_KITCHEN_GROUND:
                    //原水盆柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BASIN_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BASIN_KITCHEN_GROUND);
                        break;
                    //水盆台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BASIN_FACE_KITCHEN_GROUND:
                    //原水盆台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BASIN_FACE_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BASIN_FACE_KITCHEN_GROUND);
                        break;
                    //消毒碗柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STERILIZED_KITCHEN_GROUND:
                    //原消毒碗柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_STERILIZED_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STERILIZED_KITCHEN_GROUND);
                        break;
                    //中间台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MIDDLE_KITCHEN_GROUND:
                    //原中间台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_MIDDLE_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_MIDDLE_KITCHEN_GROUND);
                        break;
                    //转角地柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TURN_KITCHEN_GROUND:
                    //原转角地柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TURN_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TURN_KITCHEN_GROUND);
                        break;
                    //转角柜台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TURN_FACE_KITCHEN_GROUND:
                    //原转角柜台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TURN_FACE_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TURN_FACE_KITCHEN_GROUND);
                        break;
                    //橱柜全台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FULL_KITCHEN_GROUND:
                    //原橱柜全台面
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_FULL_KITCHEN_GROUND:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_FULL_KITCHEN_GROUND);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配厨房地柜产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
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
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DOOR_KITCHEN_CUPBOARD);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CONVERT_DOOR_KITCHEN_CUPBOARD);
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_KITCHEN_CUPBOARD);
                        break;
                    //油烟机柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COOKER_HOOD_KITCHEN_CUPBOARD:
                    //原油烟机柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_COOKER_HOOD_KITCHEN_CUPBOARD:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COOKER_HOOD_KITCHEN_CUPBOARD);
                        break;
                    //转角吊柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TURN_KITCHEN_CUPBOARD:
                    //原转角吊柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TURN_KITCHEN_CUPBOARD:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TURN_KITCHEN_CUPBOARD);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配厨房吊柜产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //厨房配件
            case ProductTypeValue.PRODUCT_TYPE_VALUE_KITCHEN_ACCESORIES:
                switch (productTypeSmallValue) {
                    //水槽
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WATER_CHANNEL_KITCHEN_CONFIGURATION:
                    //原水槽
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_WATER_CHANNEL_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WATER_CHANNEL_KITCHEN_CONFIGURATION);
                        break;
                    //刀叉盘
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_KNIVES_KITCHEN_CONFIGURATION:
                    //原刀叉盘
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_KNIVES_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_KNIVES_KITCHEN_CONFIGURATION);
                        break;
                    //钢抽
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STEEL_KITCHEN_CONFIGURATION:
                    //原刚抽
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_STEEL_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STEEL_KITCHEN_CONFIGURATION);
                        break;
                    //可移动置物架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COMMODITY_SHELF_KITCHEN_CONFIGURATION:
                    //原可移动置物架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_COMMODITY_SHELF_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COMMODITY_SHELF_KITCHEN_CONFIGURATION);
                        break;
                    //保鲜产品
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_RETAIN_KITCHEN_CONFIGURATION:
                    //原保鲜产品
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_RETAIN_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_RETAIN_KITCHEN_CONFIGURATION);
                        break;
                    //抹布挂架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DUSTER_KITCHEN_CONFIGURATION:
                    //原抹布挂架
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_DUSTER_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DUSTER_KITCHEN_CONFIGURATION);
                        break;
                    //分格盘
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOX_KITCHEN_CONFIGURATION:
                    //原分格盘
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BOX_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BOX_KITCHEN_CONFIGURATION);
                        break;
                    //隔间杆
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SPLIT_KITCHEN_CONFIGURATION:
                    //原隔间杆
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SPLIT_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SPLIT_KITCHEN_CONFIGURATION);
                        break;
                    //各类挂钩
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TYPE_KITCHEN_CONFIGURATION:
                    //原各类挂钩
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_TYPE_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TYPE_KITCHEN_CONFIGURATION);
                        break;
                    //柜式储物柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SAVE_KITCHEN_CONFIGURATION:
                    //原柜式储物柜
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SAVE_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SAVE_KITCHEN_CONFIGURATION);
                        break;
                    //龙头
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BIBCOCK_KITCHEN_CONFIGURATION:
                    //原龙头
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_BIBCKCK_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BIBCOCK_KITCHEN_CONFIGURATION);
                        break;
                    //橱柜把手
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HAND_KITCHEN_CONFIGURATION:
                    //原橱柜把手
                    case ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_HAND_KITCHEN_CONFIGURATION:
                        productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_HAND_KITCHEN_CONFIGURATION);
                        break;
                    default:
                        productSmallTypeList.add(productTypeSmallValue);
                        log.info(CLASS_LOG_PREFIX + "匹配厨房配件产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
                }
                break;
            //床架
            case ProductTypeValue.PRODUCT_TYPE_VALUE_BEDSTEAD:
                //床(双人床)-上下床-榻榻米-其他
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BED_BEDSTEAD);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_UPDOWN_BED_BEDSTEAD);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_TATAMI_BEDSTEAD);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_OTHER_BEDSTEAD);
                break;
            //隔断屏风
            case ProductTypeValue.PRODUCT_TYPE_VALUE_SCREEN:
                //定制隔断柜-隔断柜-屏风-屏风墙
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_SPLIT_SCREEN);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SPLIT_SCREEN);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_SCREEN);
                productSmallTypeList.add(ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WALL_SCREEN);
                break;
            default:
                productSmallTypeList.add(productTypeSmallValue);
                log.info(CLASS_LOG_PREFIX + "匹配产品小类规则->无对应大类匹配规则，应用默认小类.productTypeValue:{}, productTypeSmallValue:{}.", productTypeValue, productTypeSmallValue);
        }
        log.info(CLASS_LOG_PREFIX + "产品大类:{}, 产品小类:{}, 已匹配产品小类:{}.", new String[]{
                productTypeValue + "",
                productTypeSmallValue + "",
                JsonUtil.toJson(productSmallTypeList)
        });

        return productSmallTypeList;
    }


    /**
     * 获取产品原始小类(根据白膜小类获取产品原始小类)
     *
     * @param productTypeValue          产品大类
     * @param productTypeSmallValue     产品小类
     * @return
     */
    public int getProductOriginSmallType(int productTypeValue, int productTypeSmallValue) {

        if (0 == productTypeValue || 0 == productTypeSmallValue) {
            return 0;
        }

        //原始小类
        int originProductSmallType = productTypeSmallValue;


        //获取产品大类字典值
        List<SystemDictionaryPo> productTypeDictionaryList = systemDictionaryMetaDataStorage.querySystemDictionaryListByType(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_PRODUCTTYPE);

        //当前大类valuekey
        String nowProductValueKey = null;
        //获取当前大类valuekey
        for (SystemDictionaryPo systemDictionaryPo : productTypeDictionaryList) {
            if (productTypeValue == systemDictionaryPo.getDictionaryValue()) {
                nowProductValueKey = systemDictionaryPo.getDictionaryKey();
                break;
            }
        }

        //获取产品小类字典值
        List<SystemDictionaryPo> productSmallTypeDictionaryList = systemDictionaryMetaDataStorage.querySystemDictionaryListByType(nowProductValueKey);
        //当前小类valuekey
        String nowProductSmallValueKey = null;
        //获取当前小类valuekey
        for (SystemDictionaryPo systemDictionaryPo : productSmallTypeDictionaryList) {
            if (productTypeSmallValue == systemDictionaryPo.getDictionaryValue()) {
                nowProductSmallValueKey = systemDictionaryPo.getDictionaryKey();
                break;
            }
        }

        //检查当前小类是否为白膜
        if (!StringUtils.isEmpty(nowProductSmallValueKey) && (nowProductSmallValueKey.startsWith("basic_") || nowProductSmallValueKey.startsWith("baimo_"))) {
            //去掉valke白膜标识
            nowProductSmallValueKey = nowProductSmallValueKey.replace("basic_", "").replace("baimo_", "");
            //根据valuekey查询小类列表中是否有符合的小类
            for (SystemDictionaryPo systemDictionaryPo : productSmallTypeDictionaryList) {
                if (nowProductSmallValueKey.equals(systemDictionaryPo.getDictionaryKey())) {
                    //当前小类即为原始小类
                    originProductSmallType = systemDictionaryPo.getDictionaryValue();
                    break;
                }
            }
        }

        log.info(CLASS_LOG_PREFIX + "获取产品原始小类(根据白膜小类获取产品原始小类)完成!-productTypeValue:{}, productSmallTypeValue:{}, nowProductSmallTypeValue:{}.", new Integer[]{
                productTypeValue,
                productTypeSmallValue,
                originProductSmallType
        });

        return originProductSmallType;
    }
}


