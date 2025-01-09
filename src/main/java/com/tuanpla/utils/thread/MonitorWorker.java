/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuanpla.utils.thread;

import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author TUANPLA
 */
public class MonitorWorker {

    private static final Logger logger = LogManager.getLogger(MonitorWorker.class);
    private static final LinkedList<String> DEMON_THREAD_LIST = new LinkedList<>();            // Dem So Demon Thread
    private static final LinkedList<ThreadPoolExecutor> WORKS = new LinkedList<>();

    private static String LB_NODE = "Unset";

    public static void setNodeName(String name) {
        LB_NODE = name;
    }

    public static void addWorkQueue(final ThreadPoolExecutor work) {
        if (work == null) {
            logger.warn("MonitorWorker ThreadPoolExecutor work is null");
        }
        synchronized (WORKS) {
            WORKS.add(work);
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
            DEMON_THREAD_LIST.notify();
            logger.debug("|==> " + name + " ended...");
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
                logger.info((i++) + ". " + one + " is runing");
            }
            DEMON_THREAD_LIST.notify();
        }
    }

    public static void ShowMonitor() {
        synchronized (WORKS) {
            if (!WORKS.isEmpty()) {
                logger.info("-------------MonitorWorker [ " + LB_NODE + "  ]-----------");
                for (ThreadPoolExecutor work : WORKS) {
                    logger.info("M-Worker" + work.toString());
                }
                showDemon();
                DataQueue.showQueuesSize();
            } else {
                logger.info("WORKS QUEUE is Empty... ^.^");
            }
            WORKS.notify();
        }
    }
}
