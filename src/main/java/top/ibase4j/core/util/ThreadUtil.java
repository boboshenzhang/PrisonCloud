package top.ibase4j.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 线程辅助类
 * @author ShenHuaJie
 * @since 2018年7月27日 下午7:00:11
 */
public final class ThreadUtil {
    static Logger logger = LogManager.getLogger();

    public static void sleep(int start, int end) {
        try {
            Thread.sleep(MathUtil.getRandom(start, end).longValue());
        } catch (InterruptedException e) {
            logger.error(ExceptionUtil.getStackTraceAsString(e));
        }
    }

    public static void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage());
        }
    }

    public static ExecutorService threadPool(int core, int max, int seconds) {
        return new ThreadPoolExecutor(core, max, seconds, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }
}
