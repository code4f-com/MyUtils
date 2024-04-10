package com.tuanpla.utils.thread;

import com.tuanpla.utils.config.PublicConfig;
import com.tuanpla.utils.logging.LogUtils;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Title: WorkQueue</p>
 * <p>
 * Description: WorkQueue xu ly Data </p>
 * <p>
 * Copyright: Copyright (c) 2011</p>
 * <p>
 * Company:MSM </p>
 *
 * @author tuanpla
 * @version 3.0
 */
public class WorkQueue {

    private static Logger logger = LoggerFactory.getLogger(WorkQueue.class);

    private final int maxPoolSize;
    private int activeCount;
    private int waitTaskCount;
    private int completedTaskCount;
    private final PoolWorker[] threadPool;        // Dung de thuc thi cac Thread Truyen vao
    private final LinkedList<Runnable> THREAD_QUEUE = new LinkedList();       // Thread Queue se cho de xu ly
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkQueue(String name, int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        this.name = name;
        this.threadPool = new PoolWorker[maxPoolSize];
        //--
        for (int i = 0; i < maxPoolSize; i++) {
            threadPool[i] = new PoolWorker();
            threadPool[i].setName("[" + name + ":" + i + "]");
            threadPool[i].start();
        }
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public int getCompletedTaskCount() {
        return completedTaskCount;
    }

    public int getWaitTaskCount() {
        return waitTaskCount;
    }

    public void setWaitTaskCount(int waitTaskCount) {
        this.waitTaskCount = waitTaskCount;
    }

    /**
     * get Total Thread Execute
     *
     * @return
     */
    public long getTaskCount() {
        long n = completedTaskCount + activeCount;
        synchronized (THREAD_QUEUE) {
            n += THREAD_QUEUE.size();
            return n;
        }
    }

    /**
     * Execute Thread
     *
     * @param r
     */
    public void execute(Runnable r) {
        synchronized (THREAD_QUEUE) {
            THREAD_QUEUE.addLast(r);
            THREAD_QUEUE.notify();
        }
    }

    public void shutDown() {
        LogUtils.out(this.getName() + " starting shutdown...");
        for (PoolWorker one : threadPool) {
            // Cacs Thread dang chay trong day thi rat nguy hiem :D
            try {
                one.interrupt();
                one.interrupt = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtils.out(this.getName() + "--> shutdown completed...");
    }

    private class PoolWorker extends Thread {

        boolean interrupt = false;

        @Override
        public void run() {
            Runnable r = null;
            activeCount++;  // Ban Than Cung La 1 Thread
            while (PublicConfig.AppRunning) {
                synchronized (THREAD_QUEUE) {
                    while (THREAD_QUEUE.isEmpty() && !interrupt) {
                        try {
                            // Cho Vi Trong queue Ko co phan tu nao
//                            Tool.debug("PoolWorker " + this.getName() + " Dang cho ThreadQueue has more element...");
                            waitTaskCount++;
                            THREAD_QUEUE.wait();
                            waitTaskCount--;
                        } catch (InterruptedException timeOut) {
                            logger.warn("PoolWorker " + this.getName() + " InterruptedException [Time out]");
                        }
                    }
                    if (!interrupt) {
                        // Neu co thi se lay phan thu dau tien ra va xu ly
                        r = THREAD_QUEUE.removeFirst();
                        THREAD_QUEUE.notify();
                    } else {
                        THREAD_QUEUE.notify();
                    }
                }

                // If we don't catch RuntimeException, the pool could leak threads
                if (!interrupt && r != null) {
                    try {
                        activeCount++;          // Lay Ra 1 Thread va thuc thi Thread do
                        r.run();                // Void thi cung phai chay xong...
                    } catch (Exception e) {
                        logger.error(LogUtils.getLogMessage(e));
                        e.printStackTrace();
                    }
                    activeCount--;              // Khi thuc thi Xong thi giam di
//                    Tool.debug("Tang completedTaskCount 1");
                    completedTaskCount++;       // Tang Completed Count len 1
                }
            }
            waitTaskCount--;
            activeCount--;
//            Tool.debug("Tang completedTaskCount 2");
            completedTaskCount++;
            logger.debug(this.getName() + " is End..");
        }
    }
}
