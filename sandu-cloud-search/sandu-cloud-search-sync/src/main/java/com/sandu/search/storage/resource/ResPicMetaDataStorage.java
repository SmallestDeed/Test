package com.sandu.search.storage.resource;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.entity.elasticsearch.po.metadate.ResPicPo;
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
 * 图片资源元数据存储
 *
 * @date 20171220
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ResPicMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "图片资源元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public ResPicMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //图片资源元数据Map<picId, PicPath>
    private static Map<String, String> resPicMap = null;

    /**
     * 切换存储模式
     * update by huangsongbo 2018.9.10 不打算再用此方法,更名changeStorageMode -> changeStorageMode_old
     * 
     * @param storageMode
     */
    public void changeStorageMode_old(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            resPicMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            /*updateData();*/
            updateData_old();
        }
        log.info(CLASS_LOG_PREFIX + "图片资源存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    /**
     * 获取Map数据方法兼容
     * update by huangsongbo 2018.9.10 不打算再用此方法,更名getMap -> getMap_old
     * 
     * @param mapName
     * @param keyName
     * @return
     */
    private String getMap_old(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PIC_DATA.equals(mapName)) {
                //图片数据过多，太占用内存，所以取消图片数据的内存策略
                if (null != resPicMap && resPicMap.containsKey(keyName)) {
                    return resPicMap.get(keyName);
                } else {
                    /*
                    内存中未查询到图片数据不再需要从数据库获取,减少性能消耗
                    //初始化
                    if (null == resPicMap) {
                        resPicMap = new HashMap<>();
                    }

                    //查询数据库
                    ResPicPo resPicPo;
                    try {
                        resPicPo = metaDataService.getResPicMetaDataById(Integer.parseInt(keyName));
                    } catch (MetaDataException e) {
                        log.error(CLASS_LOG_PREFIX + "根据ID查询图片资源元数据失败: MetaDataException:{}", e);
                        throw new NullPointerException(CLASS_LOG_PREFIX + "根据ID查询图片资源元数据失败,ResPicPo is null.MetaDataException:" + e);
                    }

                    if (null == resPicPo) {
                        return null;
                    }

                    String picPath = resPicPo.getPicPath();
                    if (StringUtils.isEmpty(picPath)) {
                        return null;
                    }

                    //装入内存
                    resPicMap.put(keyName, picPath);
                    return picPath;*/
                }
            }
        }
        return null;
    }

    /**
     * 更新数据
     * 
     * update by huangsongbo 2018.9.10 不打算再用此方法,更名updateData -> updateData_old
     * 
     */
    public void updateData_old() {

        log.info(CLASS_LOG_PREFIX + "开始获取图片资源元数据元数据....");
        List<ResPicPo> resPicList;
        try {
            //获取数据
            resPicList = metaDataService.queryResPicMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取图片资源元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取图片资源元数据失败,List<ResPicPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取图片资源元数据完成,总条数:{}", (null == resPicList ? 0 : resPicList.size()));

        //临时对象
        Map<String, String> tempResPicMap = new HashMap<>();

        //转换为Map元数据
        if (null != resPicList && 0 != resPicList.size()) {
            resPicList.forEach(resPicPo -> {
                Integer id = resPicPo.getId();
                String picPath = resPicPo.getPicPath();
                if (null != id && 0 != id && !StringUtils.isEmpty(picPath)) {
                    tempResPicMap.put(id + "", picPath);
                }
            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化图片资源元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.PIC_DATA, tempResPicMap);
        log.info(CLASS_LOG_PREFIX + "图片资源元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            resPicMap = tempResPicMap;
            log.info(CLASS_LOG_PREFIX + "图片资源元数据装载内存完成....");
        }
    }

    /**
     * 根据图片ID获取图片路径
     *
     * @param picId 图片ID
     * @return
     */
    public String getPicPathByPicId(Integer picId) {
        if (null == picId || 0 == picId ) {
            return null;
        }
        
        //update by huangsongbo 2018.9.10 改为从db取图片数据
        /*return getMap(RedisConstant.PIC_DATA, picId + "");*/
        
        return metaDataService.getPicPathFromResPicById(picId);
    }

    /**
     * 根据图片ID列表获取图片路径列表
     * 
     * update by huangsongbo 2018.9.14: 加入缓存数据
     *
     * @param picIdList 图片IDList
     * @param picMap 缓存数据
     * @return
     */
    public List<String> queryPicPathListByPicIdList(List<Integer> picIdList, Map<Integer, String> picMap) {
        if (null == picIdList || 0 >= picIdList.size()) {
            return null;
        }

        List<String> picPathList = new ArrayList<>(picIdList.size());
        for (Integer picId : picIdList) {
            String picPath = null;
            if(picMap == null) {
            	picPath = getPicPathByPicId(picId);
            }else {
            	if(picMap.containsKey(picId)) {
            		picPath = picMap.get(picId);
            	}else {
            		picPath = getPicPathByPicId(picId);
            	}
            }
            if (!StringUtils.isEmpty(picPath)) {
                picPathList.add(picPath);
            }
        }
        return picPathList;
    }

    public ResPicPo queryPicPathByPicId(Integer picId) {
        log.info(CLASS_LOG_PREFIX+"开始查询单个图片数据开始");
        ResPicPo resPicPo;
        try {
            resPicPo = metaDataService.getResPicMetaDataById(picId);
        }catch (Exception e){
            log.info(CLASS_LOG_PREFIX+"查询单个图片数据失败");
            throw new NullPointerException(CLASS_LOG_PREFIX + "查询单个图片数据失败,resPicPo is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX+"开始查询单个图片数据完成"+resPicPo);
        return resPicPo;

    }


    /**
     * 根据图片ID列表获取图片路径列表
     *
     * @param picIdList 图片IDList
     * @return
     */
    public List<String> queryPicPathList(List<Integer> picIdList) {
        if (null == picIdList || 0 >= picIdList.size()) {
            return null;
        }
        List<String> picPathList = new ArrayList<>(picIdList.size());
        for (Integer picId : picIdList) {
            ResPicPo resPicPo = queryPicPathByPicId(picId);
            if (null !=resPicPo) {
                picPathList.add(resPicPo.getPicPath());
            }
        }
        return picPathList;
    }

    /**
     * 
     * @author huangsongbo
     * @param picIdList
     * @return
     */
	public Map<Integer, String> getMapByIdList(List<Integer> picIdList) {
		// 参数验证 ->start
		if(picIdList == null || picIdList.size() == 0) {
			return null;
		}
		// 参数验证 ->end
		
		List<ResPicPo> resPicPoList = getListByPicIdList(picIdList);
		
		Map<Integer, String> returnMap = new HashMap<Integer, String>();
		if(resPicPoList != null && resPicPoList.size() > 0) {
			resPicPoList.forEach(item -> returnMap.put(item.getId(), item.getPicPath()));
		}
		return returnMap;
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param picIdList
	 * @return
	 */
	private List<ResPicPo> getListByPicIdList(List<Integer> picIdList) {
		// 参数验证 ->start
		if(picIdList == null || picIdList.size() == 0) {
			return null;
		}
		// 参数验证 ->end
		
		return metaDataService.getPicListByPicIdList(picIdList);
	}

}
