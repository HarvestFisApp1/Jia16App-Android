package com.jia16.service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池
 *
 * @author jiaohongyun
 */
public class ThreadsPool {
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread tread = new Thread(r, "Dimeng theads #" + mCount.getAndIncrement());
            // 设置线程优先级
            tread.setPriority(Thread.NORM_PRIORITY - 1);
            return tread;
        }
    };
    private static int threadCount = 4;// 线程池数量
    /**
     * 线程管理
     */
    private static final Executor executor = Executors.newFixedThreadPool(threadCount, sThreadFactory);

    /**
     * 执行一个任务
     *
     * @param runnable
     * @see [类、类#方法、类#成员]
     */
    public static void executeOnExecutor(Runnable runnable) {
        executor.execute(runnable);
    }
}
