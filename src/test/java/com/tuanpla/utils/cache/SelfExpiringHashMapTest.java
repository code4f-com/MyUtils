/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of containsValue method, of class SelfExpiringHashMap.
     */
    @Test
    public void testContainsValue() {
        System.out.println("containsValue");
        Object value = null;
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        boolean expResult = false;
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class SelfExpiringHashMap.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        Object key = null;
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        Object expResult = null;
        Object result = instance.get(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of put method, of class SelfExpiringHashMap.
     */
    @Test
    public void testPut_GenericType_GenericType() {
        System.out.println("put");
        Object key = null;
        Object value = null;
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        Object expResult = null;
        Object result = instance.put(key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of put method, of class SelfExpiringHashMap.
     */
    @Test
    public void testPut_3args() {
        System.out.println("put");
        Object key = null;
        Object value = null;
        long lifeTimeMillis = 0L;
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        Object expResult = null;
        Object result = instance.put(key, value, lifeTimeMillis);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class SelfExpiringHashMap.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        Object key = null;
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        Object expResult = null;
        Object result = instance.remove(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putAll method, of class SelfExpiringHashMap.
     */
    @Test
    public void testPutAll() {
        System.out.println("putAll");
        Map m = null;
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        instance.putAll(m);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of renewKey method, of class SelfExpiringHashMap.
     */
    @Test
    public void testRenewKey() {
        System.out.println("renewKey");
        Object key = null;
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        boolean expResult = false;
        boolean result = instance.renewKey(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class SelfExpiringHashMap.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of keySet method, of class SelfExpiringHashMap.
     */
    @Test
    public void testKeySet() {
        System.out.println("keySet");
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        Set expResult = null;
        Set result = instance.keySet();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of values method, of class SelfExpiringHashMap.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        SelfExpiringHashMap instance = new SelfExpiringHashMap();
        Collection expResult = null;
        Collection result = instance.values();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of entrySet method, of class SelfExpiringHashMap.
     */
//    @Test
//    public void testEntrySet() {
//        System.out.println("entrySet");
//        SelfExpiringHashMap instance = new SelfExpiringHashMap();
//        Set<Map.Entry<K, V>> expResult = null;
//        Set<Map.Entry<K, V>> result = instance.entrySet();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
