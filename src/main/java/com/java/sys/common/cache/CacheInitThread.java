package com.java.sys.common.cache;

import com.java.sys.common.utils.Tool;

public class CacheInitThread implements Runnable {

    @Override
    public void run() {
        Tool.info("--- CacheInitThread : run()  ... ",CacheUtil.class);
        CacheUtil.init();
        Tool.info("--- CacheInitThread : run() finished. ",CacheUtil.class);
    }

}
