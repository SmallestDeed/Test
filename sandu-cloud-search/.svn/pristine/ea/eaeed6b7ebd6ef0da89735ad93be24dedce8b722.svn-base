package com.sandu.search.entity.elasticsearch.index;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 户型索引映射对象
 *
 * @date 20180109
 * @auth pengxuangang
 */
@Data
public class HouseIndexMappingData implements Serializable {

    private static final long serialVersionUID = 1337596959325570668L;

    /************************** 户型信息 ***************************/
    //户型ID
    private int houseId;
    //系统编码
    private String systemCode;
    //户编码
    private String houseCode;
    //户型编码
    private String houseDoorCode;
    //户型名称
    private String houseName;
    //户型图片
    private int housePicId;
    //户型小区ID
    private int houseLivingId;
    //户型总面积
    private String houseTotalArea;
    //户型公共编码
    private String houseCommonCode;
    //户型面积长编码
    private String houseAreaLongCode;
    //户型状态(1:公开, 0:不公开)
    private int houseStatus;

    /************************** 小区信息 ***************************/
    //小区类型
    private String livingType;
    //小区编码
    private String livingCode;
    //小区名
    private String livingName;
    //小区地址
    private String livingAddress;
    //小区描述
    private String livingDesc;
    //小区面积ID
    private String livingAreaId;
    //小区建筑面积
    private String livingBuildArea;
    //小区覆盖面积
    private String livingCoverArea;
    //小区面积长编码
    private String livingAreaLongCode;


    /************************** 地区信息 ***************************/
    //省ID
    private int zoneProvinceId;
    //省名
    private String zoneProvinceName;
    //市ID
    private int zoneCityId;
    //市名
    private String zoneCityName;
    //区ID
    private int zoneDistrictId;
    //区名
    private String zoneDistrictName;
    //区域长ID
    private List<String> zoneLongId;
    //区域长名
    private List<String> zoneLongName;
    //邮编
    private String zoneZipCode;
}
