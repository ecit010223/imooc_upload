package com.year18.imooc_upload.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：张玉辉 on 2018/5/1 10:14.
 */
@Deprecated
public class ThreadUtil {
    private static ExecutorService sExecutorService = Executors.newSingleThreadExecutor();

    public static void execute(Runnable runnable){
        sExecutorService.execute(runnable);
    }
}
