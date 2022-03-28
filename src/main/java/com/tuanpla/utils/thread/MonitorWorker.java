/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuanpla.utils.thread;

import com.tuanpla.config.PublicConfig;
import com.tuanpla.utils.date.DateProc;
import com.tuanpla.utils.logging.LogUtils;
import java.util.LinkedList;

/**
 *
 * @author TUANPLA
 */
public class MonitorWorker extends Thread {

    private static final LinkedList<String> DEMON_THREAD_LIST = new LinkedList<>();            // Dem So Demon Thread
    private static final LinkedList<WorkQueue> WORKS = new LinkedList<>();
    int delay;

    public MonitorWorker(int delaySecond) {
        this.delay = delaySecond;
        this.setName("MonitorWorker [" + DateProc.currentTimestamp()+ "]");
        MonitorWorker.addDemonName(this.getName());
    }

    public static void addWorkQueue(final WorkQueue work) {
        synchronized (WORKS) {
            MonitorWorker.WORKS.add(work);
            WORKS.notify();
        }

    }

    public static void addDemonName(String name) {
        synchronized (DEMON_THREAD_LIST) {
            DEMON_THREAD_LIST.add(name);
            DEMON_THREAD_LIST.notify();
        }

    }

    public static void removeDemonName(String name) {
        synchronized (DEMON_THREAD_LIST) {
            DEMON_THREAD_LIST.remove(name);
            LogUtils.debug("|==> " + name + " ended...");
            DEMON_THREAD_LIST.notify();
        }
    }

    public static int getDemonSize() {
        synchronized (DEMON_THREAD_LIST) {
            return DEMON_THREAD_LIST.size();
        }
    }

    public static void showDemon() {
        synchronized (DEMON_THREAD_LIST) {
            int i = 1;
            for (String one : DEMON_THREAD_LIST) {
                LogUtils.debug((i++) + ". " + one + " is runing");
            }
            DEMON_THREAD_LIST.notify();
        }
    }

    @Override
    public void run() {
        LogUtils.debug("|==> MonitorWorker Started...");
        while (PublicConfig.AppRunning) {
            showMonitor();
            try {
                Thread.sleep(delay * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MonitorWorker.removeDemonName(this.getName());
        LogUtils.debug("==> MyMonitorThread is die...[DemonThread size:" + DEMON_THREAD_LIST.size() + "]");
        if (DEMON_THREAD_LIST.size() > 0) {
            showDemon();
        }
        LogUtils.debug("=========> Xong roi Quit di....");
    }

    public static void showMonitor() {
        synchronized (WORKS) {
            if (!WORKS.isEmpty()) {
                for (WorkQueue work : WORKS) {
                    LogUtils.debug("************MonitorWorker**************");
                    LogUtils.debug(
                            String.format("M-Worker [" + work.getName() + "] [%d] Active: %d, Wait %d, Completed: %d, Task: %d",
                                    work.getMaxPoolSize(),
                                    work.getActiveCount(),
                                    work.getWaitTaskCount(),
                                    work.getCompletedTaskCount(),
                                    work.getTaskCount()
                            )
                    );
                }
                showDemon();
            }
            WORKS.notify();
        }
    }

    public void shutDown() {
        this.interrupt();
    }
}
