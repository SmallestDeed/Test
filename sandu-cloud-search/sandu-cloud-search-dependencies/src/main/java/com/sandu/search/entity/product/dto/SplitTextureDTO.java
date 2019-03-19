package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用来传输拆分材质信息
 *
 * @author huangsongbo
 */
@Data
public class SplitTextureDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /*key值*/
    private String key;
    /*name(eg:橱柜门,橱柜把手)*/
    private String name;
    /*材质区域名称*/
    private String textureRegionName;
    /**
     * 区域材质默认高度
     */
    private Integer height = 80;
    /**
     * 区域材质默认宽度
     */
    private Integer width = 80;

    /*材质list*/
    private List<ResTextureDTO> list;

    public SplitTextureDTO() {
        super();
    }

    public SplitTextureDTO(String key, String name, List<ResTextureDTO> list) {
        super();
        this.key = key;
        this.name = name;
        this.list = list;
    }

    public SplitTextureDTO(String key, String name, String textureRegionName, List<ResTextureDTO> list) {
        super();
        this.key = key;
        this.name = name;
        this.textureRegionName = null != textureRegionName ? textureRegionName : "";
        this.list = list;
    }

    public SplitTextureDTO(String key, String name, String textureRegionName, Integer height, Integer width, List<ResTextureDTO> list) {
        super();
        this.key = key;
        this.name = name;
        this.textureRegionName = null != textureRegionName ? textureRegionName : "";
        this.height = height;
        this.width = width;
        this.list = list;
    }

    public class ResTextureDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        /*key*/
        private String key;
        /*产品id*/
        private Integer productId;
        /*材质id*/
        private Integer id;
        /*材质路径*/
        private String path;
        /*材质属性(高光,亚光..)*/
        private Integer textureAttrValue;
        /*材质宽*/
        private Integer textureWidth;
        /*材质高*/
        private Integer textureHeight;
        /*材质铺法*/
        private String laymodes;
        /*材质球文件路径*/
        private String materialPath;
        /*品牌名称*/
        private String brandName;
        /*品牌id*/
        private String brandId;
        /*材质编号*/
        private String textureCode;

        public String getTextureCode() {
            return textureCode;
        }

        public void setTextureCode(String textureCode) {
            this.textureCode = textureCode;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getMaterialPath() {
            return materialPath;
        }

        public void setMaterialPath(String materialPath) {
            this.materialPath = materialPath;
        }

        private String normalParam;

        private String normalPath;

        public ResTextureDTO() {
            super();
        }

        public ResTextureDTO(Integer id, String path, Integer textureAttrValue, Integer textureWidth,
                             Integer textureHeight, String laymodes, String materialPath, String normalParam, String normalPath) {
            super();
            this.id = id;
            this.path = path;
            this.textureAttrValue = textureAttrValue;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.laymodes = laymodes;
            this.materialPath = materialPath;
            this.normalParam = normalParam;
            this.normalPath = normalPath;
        }

        public ResTextureDTO(Integer id, String path, Integer textureAttrValue, Integer textureWidth,
                             Integer textureHeight, String laymodes, String materialPath, String normalParam, String normalPath,
                             Integer brandId, String brandName, String textureCode) {
            super();
            this.id = id;
            this.path = path;
            this.textureAttrValue = textureAttrValue;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.laymodes = laymodes;
            this.materialPath = materialPath;
            this.normalParam = normalParam;
            this.normalPath = normalPath;
            this.brandId = null != brandId ? brandId + "" : "";
            this.brandName = null != brandName ? brandName : "";
            this.textureCode = null != textureCode ? textureCode : "";
        }

        public String getNormalParam() {
            return normalParam;
        }

        public void setNormalParam(String normalParam) {
            this.normalParam = normalParam;
        }

        public String getNormalPath() {
            return normalPath;
        }

        public void setNormalPath(String normalPath) {
            this.normalPath = normalPath;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLaymodes() {
            return laymodes;
        }

        public void setLaymodes(String laymodes) {
            this.laymodes = laymodes;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getTextureAttrValue() {
            return textureAttrValue;
        }

        public void setTextureAttrValue(Integer textureAttrValue) {
            this.textureAttrValue = textureAttrValue;
        }

        public Integer getTextureWidth() {
            return textureWidth;
        }

        public void setTextureWidth(Integer textureWidth) {
            this.textureWidth = textureWidth;
        }

        public Integer getTextureHeight() {
            return textureHeight;
        }

        public void setTextureHeight(Integer textureHeight) {
            this.textureHeight = textureHeight;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

}
