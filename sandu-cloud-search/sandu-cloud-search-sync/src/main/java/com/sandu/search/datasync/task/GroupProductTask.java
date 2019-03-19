package com.sandu.search.datasync.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 同步组合产品数据任务
 *
 * @date 20180918
 * @auth gaoj
 */
@Slf4j
@Component
public class GroupProductTask {
    private final String CLASS_LOG_PREFIX = "同步组合产品数据任务";

    //增量任务执行次数
    private static int incrTaskExcuteCount = 0;
    //全量任务执行次数
    private static int fullTaskExcuteCount = 0;
    //是否正在执行全量更新（作用：1.避免同时进行两个全量更新，2.避免执行全量更新的同时执行增量更新）
    public static boolean isRunningFullTask = false;


}
