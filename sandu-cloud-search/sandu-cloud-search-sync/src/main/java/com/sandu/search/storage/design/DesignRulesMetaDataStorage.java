package com.sandu.search.storage.design;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.dao.DesignRulesMapper;
import com.sandu.search.entity.product.dto.DesignRules;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class DesignRulesMetaDataStorage {

    private static final String CLASS_LOG_PREFIX = "设计规则元数据存储";
    // 默认为缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    @Autowired
    private DesignRulesMapper designRulesMapper;
    @Autowired
    private RedisService redisService;

    private static Map<String, String> groupProductDesignRulesMap = null;

    // 切换存储模式
    public void changeStorageMode(Integer storageMode) {
        if (StorageComponent.MEMORY_MODE == storageMode) {
            //内存模式，清空内存map，然后获取元数据，然后存入map和缓存
            STORAGE_MODE = storageMode;

            updateData();
        } else if (StorageComponent.CACHE_MODE == storageMode) {
            //缓存模式
            STORAGE_MODE = storageMode;
            //清空内存占用
            groupProductDesignRulesMap = null;
        }
    }

    /**
     * 更新数据
     */
    public void updateData() {
        log.info(CLASS_LOG_PREFIX + "更新元数据开始.....");

        List<DesignRules> designRulesList = designRulesMapper.queryGroupProductDesignRulesMetaData();

        if (null == designRulesList || designRulesList.size() <= 0) {
            log.error(CLASS_LOG_PREFIX + "获取组合产品设计规则元数据为空。。。。。");
            return;
        }
        log.info(CLASS_LOG_PREFIX + "获取组合产品设计规则元数据完成：总条数：{}", designRulesList.size());

        Map<String, List<DesignRules>> tempDesignRulesMap = new HashMap<>();

        for (DesignRules designRules : designRulesList) {
            if (tempDesignRulesMap.containsKey(designRules.getRulesLevel())) {
                List<DesignRules> rulesList = tempDesignRulesMap.get(designRules.getRulesLevel());
                List<DesignRules> newRulesList = new ArrayList<>();
                newRulesList.addAll(rulesList);
                newRulesList.add(designRules);
                tempDesignRulesMap.put(designRules.getRulesLevel(), newRulesList);
            } else {
                tempDesignRulesMap.put(designRules.getRulesLevel(), Collections.singletonList(designRules));
            }
        }

        Map<String, String> designRulesJsonMap = new HashMap<>();
        tempDesignRulesMap.forEach((k, v) -> designRulesJsonMap.put(k, JsonUtil.toJson(v)));

        redisService.addMapCompatible(RedisConstant.GROUP_PRODUCT_DESIGN_RULES_DATA, designRulesJsonMap);
        log.info(CLASS_LOG_PREFIX + "组合产品设计规则装载缓存完成。。。");

        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            groupProductDesignRulesMap = designRulesJsonMap;
            log.info(CLASS_LOG_PREFIX + "组合产品设计规则装载内存完成。。。");
        }
    }

    /**
     * 获取map的不同方式
     *
     * @return
     */
    public Map<String, List<DesignRules>> getMap() {
        Map<String, String> designRulesJsonMap;
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE && groupProductDesignRulesMap != null) {
            // 内存中存在就从内存中获取
            designRulesJsonMap = groupProductDesignRulesMap;
        } else {
            // 内存不存在从缓存中获取
            designRulesJsonMap = redisService.getMap(RedisConstant.GROUP_PRODUCT_DESIGN_RULES_DATA);
        }

        if (null == designRulesJsonMap || 0 >= designRulesJsonMap.size()) {
            log.error(CLASS_LOG_PREFIX + "获取内存或缓存的组合产品设计规则为空。。。。");
            return null;
        }
        Map<String, List<DesignRules>> designRulesMap = new HashMap<>();

        designRulesJsonMap.forEach((k, v) ->
                designRulesMap.put(k, JsonUtil.fromJson(v, new TypeToken<List<DesignRules>>() {
                }.getType()))
        );

        return designRulesMap;
    }

    /**
     * 根据level 和 mainValue 获取设计规则集合
     *
     * @param designRules
     * @return
     */
    public List<DesignRules> getByLevelAndMainValue(DesignRules designRules) {
        log.info(CLASS_LOG_PREFIX + "根据level 和 mainValue 获取设计规则集合开始。。。");
        Map<String, List<DesignRules>> designRulesMap = this.getMap();
        if (null == designRulesMap || 0 >= designRulesMap.size()) {
            return null;
        }
        log.info(CLASS_LOG_PREFIX + "获取designRulesMap完成，总条数：{}", designRulesMap.size());

        String rulesLevel = designRules.getRulesLevel();
        String rulesMainValue = designRules.getRulesMainValue();
        log.info(CLASS_LOG_PREFIX + "level:{}, mainValue:{}", rulesLevel, rulesMainValue);

        if (designRulesMap.containsKey(rulesLevel)) {
            List<DesignRules> designRulesList = new ArrayList<>();
            List<DesignRules> rulesList = designRulesMap.get(rulesLevel);
            for (DesignRules rules : rulesList) {
                if (rulesMainValue.equals(rules.getRulesMainValue())) {
                    designRulesList.add(rules);
                }
            }
            return designRulesList;
        }
        return null;
    }

    /**
     * 根据level 和 mainObj 获取设计规则集合
     *
     * @param designRules
     * @return
     */
    public List<DesignRules> getByLevelAndMainObj(DesignRules designRules) {
        log.info(CLASS_LOG_PREFIX + "根据level 和 mainObj 获取设计规则集合开始。。。");
        Map<String, List<DesignRules>> designRulesMap = this.getMap();
        if (null == designRulesMap || 0 >= designRulesMap.size()) {
            return null;
        }
        log.info(CLASS_LOG_PREFIX + "获取designRulesMap完成，总条数：{}", designRulesMap.size());

        String rulesLevel = designRules.getRulesLevel();
        String rulesMainObj = designRules.getRulesMainObj();
        log.info(CLASS_LOG_PREFIX + "level:{}, mainObj:{}", rulesLevel, rulesMainObj);

        if (designRulesMap.containsKey(rulesLevel)) {
            List<DesignRules> designRulesList = new ArrayList<>();
            List<DesignRules> rulesList = designRulesMap.get(rulesLevel);
            for (DesignRules rules : rulesList) {
                if (rulesMainObj.equals(rules.getRulesMainObj())) {
                    designRulesList.add(rules);
                }
            }
            return designRulesList;
        }
        return null;
    }

}
