package com.tuanpla.utils.thread;

import com.tuanpla.utils.common.PublicConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Queue {

    protected LinkedList queue = null;
    protected Object monitor = null;
    private static int count = 0;
    private String name = "queue_" + ++count;
    private static ArrayList queueList = new ArrayList();

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    /**
     *
     * @return
     */
    public static String showQueuesSize() {
        String response = "Size of Queues:\n";
        Queue q;
        for (int i = 0; i < queueList.size(); i++) {
            q = (Queue) queueList.get(i);
            if (q != null) {
                response += q.getName() + ": " + q.size() + "\n";
            }
        }
        return response;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public Queue() {
        this.monitor = this;
        this.queue = new LinkedList();
        queueList.add(this);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public Queue(String name) {
        this.monitor = this;
        this.queue = new LinkedList();
        this.name = name;

        queueList.add(this);
    }

    /**
     * This method is used by a consummer. If you attempt to remove an object
     * from an queue is empty queue, you will be blocked (suspended) until an
     * object becomes available to remove. A blocked thread will thus wake up.
     *
     * @return the first object (the one is removed).
     */
    public Object dequeue() {
        synchronized (monitor) {
            while (queue.isEmpty() && PublicConfig.AppRunning) { //Threads are blocked
                try { //if the queue is empty.
                    monitor.wait(); //wait until other thread call notify().
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            Object item = queue.removeFirst();
            return item;
        }
    }

    public void enqueue(Object item) {
        synchronized (monitor) {
            queue.addLast(item);
            monitor.notifyAll();
        }
    }

    public int size() {
        synchronized (monitor) {
            return queue.size();
        }
    }

    public boolean isEmpty() {
        synchronized (monitor) {
            return queue.isEmpty();
        }
    }

    public boolean contain(Object obj) {
        boolean flag = false;
        synchronized (monitor) {
            for (Iterator<Object> it = queue.iterator(); it.hasNext();) {
                Object one = it.next();
                if (one.equals(obj)) {
                    flag = true;
                    break;
                }
            }
            monitor.notifyAll();
            return flag;
        }
    }

    public void ShutDown() {
        monitor.notifyAll();
    }

    protected Collection dequeueAll() {
        List list = null;
        synchronized (monitor) {
            list = Arrays.asList(queue.toArray());
            queue.clear();
        }
        return list;
    }

    protected void enqueueAll(Collection c) {
        synchronized (monitor) {
            queue.addAll(c);
            monitor.notifyAll();
        }
    }
}
