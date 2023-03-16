/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.common;

import com.tuanpla.utils.logging.LogUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author tuanpla
 */
public class MyUtils {

    static final Logger logger = LoggerFactory.getLogger(MyUtils.class);
    // https://stackoverflow.com/questions/19456313/simple-timeout-in-java
    static final Duration timeout = Duration.ofSeconds(30);
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    final Future<String> handler = executor.submit(new Callable() {
        @Override
        public String call() throws Exception {
            return requestDataFromModem();
        }

        private String requestDataFromModem() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    });

    public void option_1() {
        executor.schedule(() -> {
            handler.cancel(true);
        },
                timeout.toMillis(),
                TimeUnit.MILLISECONDS);

        executor.shutdownNow();
    }

    public void option_2() throws InterruptedException, ExecutionException {
        ExecutorService _executor = Executors.newSingleThreadExecutor();
        final Future<String> _handler = _executor.submit(new Callable() {
            @Override
            public String call() throws Exception {
                return requestDataFromModem();
            }

            private String requestDataFromModem() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        try {
            _handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            _handler.cancel(true);
        }

        _executor.shutdownNow();
    }

    /**
     * Neu thoi gian hien tai <= thoi gian timeout => false else => true
     *
     * @param lastTime
     * @param secondTimeOut
     * @return
     */
    public static boolean timeOut(long lastTime, int secondTimeOut) {
        long current = System.currentTimeMillis();
        boolean result = lastTime + (secondTimeOut * 1000) < current;
        LogUtils.debug("timeOut.result: " + result);
        return result;
    }

    public static void sleepSecond(int second) {
        long start = System.currentTimeMillis();
        while (true) {
            long runTime = System.currentTimeMillis();
            long distance = runTime - start;
            if (distance > second * 1000) {
                break;
            }
        }
    }

    public static void sleepSecond(long delay) throws InterruptedException {
        TimeUnit.SECONDS.sleep(delay);
    }

    public static Map<Object, Object> MyMap(Object key, Object val) {
        if (!MyString.isEmpty(key) && !MyString.isEmpty(val)) {
            Map<Object, Object> result = new HashMap<>();
            result.put(key, val);
            return result;
        }
        return null;
    }

    public void debugValue() {
        try {
            Class objClass = this.getClass();
            // Get the public methods associated with this class.
            Method[] methods = objClass.getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith("get") || methodName.startsWith("is")) {
                    String fieldName = getFieldName(method);
                    if (!MyString.isEmpty(fieldName)) {
//                        if (fieldName.equals("brand") || fieldName.equals("class")) {
//                            continue;
//                        }
                        System.out.print(fieldName + ":" + method.invoke(this) + "\n");
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
    }

    public static String getFieldName(Method method) {
        try {
            Class<?> clazz = method.getDeclaringClass();
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                if (method.equals(pd.getWriteMethod()) || method.equals(pd.getReadMethod())) {
                    return pd.getName();
                }
            }
        } catch (Exception e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return null;
    }
}
