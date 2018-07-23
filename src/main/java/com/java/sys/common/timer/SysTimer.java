package com.java.sys.common.timer;

import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.utils.Tool;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SysTimer {

    /**
     * 30分钟执行一次
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void runMin30(){
        Tool.info("--- runMin30 start",SysTimer.class);
        CacheUtil.refreshMenu();
        CacheUtil.refreshRealm();
        Tool.info("--- runMin30 end",SysTimer.class);
    }



}
