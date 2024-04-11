/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.common;

import com.tuanpla.utils.config.PublicConfig;
import com.tuanpla.utils.logging.LogUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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

    private static Logger logger = LoggerFactory.getLogger(MyUtils.class);
//    static final Logger logger = LogManager.getLogger(MyUtils.class);
    private static final Random RANDOM = new SecureRandom();
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

    public static int randomInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static long randomLong() {
        return RANDOM.nextLong();
    }

    /**
     * Assert that an object is not {@code null}.
     * <pre class="code">Assert.notNull(clazz, "The class must not be
     * null");</pre>
     *
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static void notNull(@Nullable Object object, String message) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a boolean expression, throwing an {@code IllegalArgumentException}
     * if the expression evaluates to {@code false}.
     * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than
     * zero");</pre>
     *
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static boolean notDel() {
        return !PublicConfig.ISDEL;
    }
}
