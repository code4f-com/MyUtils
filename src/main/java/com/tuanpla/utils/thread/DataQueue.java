package com.tuanpla.utils.thread;

import com.tuanpla.utils.date.DateProc;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>
 * Title: </p>
 * <p>
 * Description: Queue xu ly Data </p>
 * <p>
 * Copyright: Copyright (c) 2011</p>
 * <p>
 * Company:PG Media </p>
 *
 * @author TuanPLA
 * @version 3.0
 * @param <V>
 */
public class DataQueue<V> {

    private static final Logger logger = LogManager.getLogger(DataQueue.class);
    private static final Queue<DataQueue<?>> QUEUE_LIST = new ConcurrentLinkedQueue<>(); // Theo dõi xem đang có bao nhiêu DataQueue được tạo trên hệ thống
    private static final int CAPACITY = 200 * 1000;

    private final AtomicInteger totalCount = new AtomicInteger(0);
    private final AtomicInteger processCount = new AtomicInteger(0);

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition(); // để quản lý tình huống hàng đợi đầy:
    protected LinkedList<V> dataQueue = null;
    private final String name;
    private final String startTime;
    // Sử dụng generics để tránh cảnh báo

    @SuppressWarnings("LeakingThisInConstructor")
    public DataQueue(String name) {
        this.dataQueue = new LinkedList<>();
        this.name = name;
        this.startTime = DateProc.currentDate("dd/MM/yyyy HH:mm");
        QUEUE_LIST.add(this);
    }

    public void removeQueue() {
        QUEUE_LIST.remove(this);
    }

    public static void showQueuesSize() {
        StringBuilder response = new StringBuilder("[----Queues Size:---\n");
        for (DataQueue<?> oneQueue : QUEUE_LIST) {
            response.append(oneQueue.name).append(": ").append(oneQueue.size())
                    .append("; Start-time:").append(oneQueue.startTime)
                    .append("; All task:").append(oneQueue.totalCount.get())
                    .append("; process:").append(oneQueue.processCount.get())
                    .append("; peding:").append(oneQueue.size())
                    .append("\n");
        }
        response.append("----End Queue Size-------]");
        logger.info(response.toString());
    }

    public void enqueue(V item) throws InterruptedException {
        lock.lock();
        try {
            while (dataQueue.size() >= CAPACITY) {
//                notFull.await(); // Optional: Use another condition for full queue
                if (!notFull.await(10, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Timeout while waiting for space in the queue.");
                }
            }
            if (item != null) {
                dataQueue.addLast(item);
                totalCount.incrementAndGet();
                notEmpty.signal(); // Notify one waiting thread - Thông báo cho các consumer
            }
        } finally {
            lock.unlock();
        }
    }

    public void enqueueFirst(V item) throws InterruptedException {
        lock.lock();
        try {
            while (dataQueue.size() >= CAPACITY) {
                notFull.await(); // Optional: Use another condition for full queue
            }
            if (item != null) {
                dataQueue.addFirst(item);
                totalCount.incrementAndGet();
                notEmpty.signal(); // Notify one waiting thread - Thông báo cho các consumer
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * This method is used by a consummer.If you attempt to remove an object
     * from an dataQueue is empty dataQueue, you will be blocked (suspended)
     * until an object becomes available to remove. A blocked thread will thus
     * wake up.
     *
     * @return the first object (the one is removed).
     * @throws java.lang.InterruptedException
     */
    public V dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (dataQueue.isEmpty()) {
                notEmpty.await();   // Chờ đến khi có dữ liệu
            }
            processCount.incrementAndGet();
            notFull.signal(); // Thông báo cho các producer
            return dataQueue.removeFirst();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return dataQueue.size();
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return dataQueue.isEmpty();
        } finally {
            lock.unlock();
        }

    }

    public Collection<V> dequeueAll() {
        List<V> list;
        lock.lock();
        try {
            list = new ArrayList<>(dataQueue);
            processCount.addAndGet(dataQueue.size());
            dataQueue.clear();
        } finally {
            lock.unlock();
        }
        return list;
    }

    public boolean contain(V obj) {
        lock.lock();
        try {
            notEmpty.signal();
            return obj != null && dataQueue.contains(obj);
        } finally {
            lock.unlock();
        }
    }
}
