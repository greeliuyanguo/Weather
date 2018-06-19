package cn.com.gree.weather.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author:liuyanguo
 * Date:2017/11/22
 * Time:9:28
 * Description:线程池的工具类
 */

public class ThreadPoolUtil {

    private static final String TAG = "ThreadPoolUtil";

    private static ThreadPoolUtil sInstance = null;
    private ExecutorService mFixedThreadPool;
    private ExecutorService mCachedThreadPool;
    private ExecutorService mSingleThreadPool;
    private ScheduledExecutorService mScheduledThreadPool;
    private ThreadPoolType mType = ThreadPoolType.FIXED_THREAD_POOL;
    private int mCorePoolSize = Runtime.getRuntime().availableProcessors() + 1;

    private ThreadPoolUtil() {
        mFixedThreadPool = Executors.newFixedThreadPool(mCorePoolSize);
        mCachedThreadPool = Executors.newCachedThreadPool();
        mSingleThreadPool = Executors.newSingleThreadExecutor();
        mScheduledThreadPool = Executors.newScheduledThreadPool(mCorePoolSize);
    }

    public static ThreadPoolUtil getInstance() {
        if (null == sInstance) {
            synchronized (ThreadPoolUtil.class) {
                if (null == sInstance) {
                    sInstance = new ThreadPoolUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 设置线程池种类
     *
     * @param type
     */
    public ThreadPoolUtil setThreadPoolType(ThreadPoolType type) {
        this.mType = type;
        return sInstance;
    }

    /**
     * 用Callable提交到线程池
     *
     * @param callable
     * @return
     */
    public Object submit(Callable<Object> callable) {
        if (null != callable) {
            Future<Object> future = mFixedThreadPool.submit(callable);
            LogUtil.d(TAG, "submited a request by Callable...");
            try {
                Object result = future.get();
                return result;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 用Runnable提交到线程池
     *
     * @param runnable
     */
    public void submit(Runnable runnable) {
        if (null != runnable) {
            switch (mType) {
                case FIXED_THREAD_POOL:
                    mFixedThreadPool.submit(runnable);
                    break;
                case CACHED_THREAD_POOL:
                    mCachedThreadPool.submit(runnable);
                    break;
                case SINGLE_THREAD_POOL:
                    mSingleThreadPool.submit(runnable);
                    break;
            }
            LogUtil.d(TAG, "submited a request by Runnable...");
        }
    }

    /**
     * 提交Runnable通过execute()方法
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (null != runnable) {
            switch (mType) {
                case FIXED_THREAD_POOL:
                    mFixedThreadPool.execute(runnable);
                    break;
                case CACHED_THREAD_POOL:
                    mCachedThreadPool.execute(runnable);
                    break;
                case SINGLE_THREAD_POOL:
                    mSingleThreadPool.execute(runnable);
                    break;
            }
            LogUtil.d(TAG, "executed a request by runnable...");
        }
    }

    /**
     * 清空线程池里面的线程
     *
     * @param type
     */
    public void clearThreadPool(ThreadPoolType type) {
        switch (type) {
            case FIXED_THREAD_POOL:
                break;
            case CACHED_THREAD_POOL:
                break;
            case SINGLE_THREAD_POOL:
                break;
        }
    }

    /**
     * 延迟指定时间之后执行
     *
     * @param runnable
     * @param millisTime
     * @param timeUnit
     */
    public void scheduleDelay(Runnable runnable, long millisTime, TimeUnit timeUnit) {
        if (null != runnable) {
            mScheduledThreadPool.schedule(runnable, millisTime, timeUnit);
        } else {
            throw new NullPointerException("Runnable cannnot be null");
        }
    }

    /**
     * 延迟delay时间之后，周期性地执行任务
     *
     * @param runnable
     * @param delay
     * @param peirod
     * @param timeUnit
     */
    public void scheduleDelayPerPeriod(Runnable runnable, long delay, long peirod, TimeUnit timeUnit) {
        if (null != runnable) {
            mScheduledThreadPool.scheduleAtFixedRate(runnable, delay, peirod, timeUnit);
        } else {
            throw new NullPointerException("Runnable cannnot be null");
        }
    }

    /**
     * 线程池的种类
     */
    public enum ThreadPoolType {
        FIXED_THREAD_POOL,
        CACHED_THREAD_POOL,
        SCHEDULED_THREAD_POOL,
        SINGLE_THREAD_POOL
    }
}