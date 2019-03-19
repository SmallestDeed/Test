package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色菜单数据持久化对象
 *
 * @date 20180801
 * @auth xiaoxc
 */
@Data
public class SysRoleFuncPo implements Serializable {

    private static final long serialVersionUID = 2485108070755202145L;

    //菜单ID
    private int funcId;
    //菜单Code
    private String funcCode;
    //角色ID
    private int roleId;
    //角色Code
    private String roleCode;

}
