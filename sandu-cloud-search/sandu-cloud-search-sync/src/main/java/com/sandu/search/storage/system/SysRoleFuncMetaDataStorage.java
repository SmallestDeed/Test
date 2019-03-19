package com.sandu.search.storage.system;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.SysRoleFuncPo;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 角色菜单元数据存储
 *
 * @date 20180709
 * @auth xiaoxc
 */
@Slf4j
@Component
public class SysRoleFuncMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "角色菜单元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public SysRoleFuncMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //角色菜单元数据Map
    private static Map<String, String> sysRoleFuncMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            sysRoleFuncMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //areaMap = redisService.getMap(RedisConstant.AREA_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "角色菜单存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    @SuppressWarnings("all")
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.SYS_ROLE_FUNC_DATA.equals(mapName)) {
                return sysRoleFuncMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        log.info(CLASS_LOG_PREFIX + "开始获取角色菜单元数据....");
        //角色菜单元数据
        List<SysRoleFuncPo> roleFuncPoList;
        try {
            roleFuncPoList = metaDataService.queryRoleFuncMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取角色菜单元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取角色菜单元数据失败,List<SysUserPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取角色菜单元数据完成,总条数:{}", (null == roleFuncPoList ? 0 : roleFuncPoList.size()));

        Map<String, List<String>> roleFuncPoMap = new HashMap<>(roleFuncPoList.size());
        //转换Map
        if (null != roleFuncPoList && 0 != roleFuncPoList.size()) {
            //遍历Map切换数据格式
            roleFuncPoList.forEach(roleFuncPo -> {
                //菜单编码
                String funcCode = roleFuncPo.getFuncCode();
                //角色编码
                String roleCode = roleFuncPo.getRoleCode();
                //菜单角色
                if (roleFuncPoMap.containsKey(funcCode)) {
                    List<String> roleCodeList = new ArrayList<>();
                    roleCodeList.add(roleCode);
                    roleCodeList.addAll(roleFuncPoMap.get(funcCode));
                    roleFuncPoMap.put(funcCode, roleCodeList);
                } else {
                    roleFuncPoMap.put(funcCode, Collections.singletonList(roleCode));
                }
            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化角色菜单元数据完成....");

        //转Json数据
        Map<String, String> roleFuncJsonMap = new HashMap<>(roleFuncPoMap.size());
        roleFuncPoMap.forEach((k,v) -> roleFuncJsonMap.put(k, JsonUtil.toJson(v)));

        //装载缓存
        redisService.addMapCompatible(RedisConstant.SYS_ROLE_FUNC_DATA, roleFuncJsonMap);
        log.info(CLASS_LOG_PREFIX + "角色菜单元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            sysRoleFuncMap = roleFuncJsonMap;
            log.info(CLASS_LOG_PREFIX + "角色菜单元数据装载内存完成....");
        }
    }

    /**
     * 根据菜单编码查询角色编码集合
     *
     * @param funcCode 菜单Code
     * @return
     */
    public List<String> getRoleListByFuncCode(String funcCode) {
        if (StringUtils.isEmpty(funcCode)) {
            return null;
        }

        String roleCodeListStr = getMap(RedisConstant.SYS_ROLE_FUNC_DATA, funcCode);
        if (StringUtils.isEmpty(roleCodeListStr)) {
            return null;
        }

        return JsonUtil.fromJson(roleCodeListStr, new TypeToken<List<String>>() {}.getType());
    }
}
