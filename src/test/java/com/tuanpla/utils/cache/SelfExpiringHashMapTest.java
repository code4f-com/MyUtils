/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.cache;

import com.tuanpla.config.PublicConfig;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author tuanp
 */
public class SelfExpiringHashMapTest {

    static SelfExpiringHashMap instance;

    public SelfExpiringHashMapTest() {
        System.out.println("---> Contructer <---");
        instance = new SelfExpiringHashMap();
    }

    @BeforeAll
    public static void setUpClass() {
        System.out.println("BeforeAll --> setUpClass");
    }

    @AfterAll
    public static void tearDownClass() {
        instance = null;
        System.out.println("AfterAll --> tearDownClass");
    }

    @BeforeEach
    public void setUp() {
        System.out.println("BeforeEach --> setUp");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("AfterEach --> tearDown");
        instance.clear();
        System.out.println("instance clear -> size:" + instance.size());
    }

    /**
     * Test of size method, of class SelfExpiringHashMap.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        instance.put("testSize", "1");
        int expResult = 1;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class SelfExpiringHashMap.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        instance.put("testIsEmpty", "testIsEmpty");
        // reuslt
        boolean expResult = false;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of containsKey method, of class SelfExpiringHashMap.
     */
    @Test
    public void testContainsKey() {
        System.out.println("containsKey");

        Object key = "testIsEmpty";

        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of containsValue method, of class SelfExpiringHashMap.
     */
    @Test
    public void testContainsValue() {
        System.out.println("containsValue");
        Object value = "testContainsValue";
        instance = new SelfExpiringHashMap();
        instance.put("key", value);
        boolean expResult = true;
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class SelfExpiringHashMap.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        Object key = "key";
        instance = new SelfExpiringHashMap();
        instance.put(key, key);
        Object expResult = key;
        Object result = instance.get(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of remove method, of class SelfExpiringHashMap.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        Object key = "remove";
        instance = new SelfExpiringHashMap();
        instance.put("remove", "value");
        Object expResult = "value";
        Object result = instance.remove(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of putAll method, of class SelfExpiringHashMap.
     */
    @Test
    public void testPutAll() {
        System.out.println("putAll");
        Map m = new HashMap();
        m.put("a", "b");
        instance = new SelfExpiringHashMap();
        instance.putAll(m);
    }

    /**
     * Test of renewKey method, of class SelfExpiringHashMap.
     */
    @Test
    public void testReNewKey() {
        System.out.println("renewKey");
        Object key = "renewKey";
        instance = new SelfExpiringHashMap(PublicConfig.DEFAULT_EXPIRE_TIME);
        instance.put(key, "valueRenewKey");
        boolean _result = instance.reNewKey(key);
        boolean expResult = true;
        assertEquals(expResult, _result);
        _result = instance.reNewKey(key);
        assertEquals(expResult, _result);
    }

    /**
     * Test of clear method, of class SelfExpiringHashMap.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        instance = new SelfExpiringHashMap();
        instance.put("key", "key");
        int size = instance.size();
        instance.clear();
        assertEquals(1, size);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of keySet method, of class SelfExpiringHashMap.
     */
    @Test
    public void testKeySet() {
        System.out.println("keySet");
        instance = new SelfExpiringHashMap();
        instance.put("key", "value");
        Set expResult = new HashSet();
        expResult.add("key");
        Set result = instance.keySet();
        assertEquals(expResult, result);
    }

    /**
     * Test of values method, of class SelfExpiringHashMap.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        instance = new SelfExpiringHashMap();
        instance.put("key1", "a");
        instance.put("key2", "b");
        Collection expResult = null;
        Collection result = instance.values();
        result.forEach(t -> System.out.println(t));
        System.out.println(result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
