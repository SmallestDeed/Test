package com.sandu.search.entity.product.dto;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class SplitTextureInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /*模块key值*/
    private String key;
    /*模块name:(橱柜门,橱柜把手)*/
    private String name;
    /*材质区域名称*/
    private String textureRegionName;
    /*材质ids*/
    private String textureIds;
    /*默认材质id*/
    private Integer defaultId;

    /**
     * 区域材质默认高度
     */
    private Integer height;
    /**
     * 区域材质默认宽度
     */
    private Integer width;

    /**
     * "贴图区域内可替换贴图信息")
     */
    private List<AffectTextures> affectTextures;

    public void setTextureRegionName(String textureRegionName) {
        this.textureRegionName = StringUtils.isNotBlank(textureRegionName) ? textureRegionName : "";
    }

    //转换对象
    public SplitTextureDTO getSplitTextureDTO() {
        SplitTextureDTO splitTextureDTO = new SplitTextureDTO();
        splitTextureDTO.setKey(this.key);
        splitTextureDTO.setName(this.name);
        splitTextureDTO.setTextureRegionName(this.textureRegionName);
        splitTextureDTO.setHeight((null == this.height || 0 == this.height) ? 80 : this.height);
        splitTextureDTO.setWidth((null == this.width || 0 == this.width) ? 80 : this.width);
        return splitTextureDTO;
    }
}
