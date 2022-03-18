/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.tuanpla.utils.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author tuanpla
 */
public class MyUtilsTest {

    public MyUtilsTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getDecimalFrom26 method, of class MyUtils.
     */
    @Test
    public void testGetDecimalFrom26() {
        System.out.println("getDecimalFrom26");
        String hex = "CEXHO";
        long expResult = 1000000;
        long result = MyUtils.getDecimalFrom26(hex);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of decimalTo26 method, of class MyUtils.
     */
    @Test
    public void testDecimalTo26() {
        System.out.println("decimalTo26");
        long d = 1000000L;
        String expResult = "CEXHO";
        String result = MyUtils.decimalTo26(d);
        System.out.println("----> Result:" + result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of decToHex method, of class MyUtils.
     */
    @Test
    public void testDecToHex() {
        System.out.println("decToHex");
        int dec = 123450;
        String expResult = "0001E23A";
        String result = MyUtils.decToHex(dec);
        System.out.println("-->testDecToHex result:" + result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    @Test
    public void testHexToDecimal() {
        System.out.println("hexToDecimal");
        String hex = "0001E23A";
        int expResult = 123450;
        int result = MyUtils.hexToDecimal(hex);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of timeOut method, of class MyUtils.
     */
    @Test
    public void testTimeOut() {
        System.out.println("timeOut");
        long lastTime = 0L;
        int secondTimeOut = 0;
        boolean expResult = false;
        boolean result = MyUtils.timeOut(lastTime, secondTimeOut);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of longToDouble method, of class MyUtils.
     */
    @Test
    public void testLongToDouble() {
        System.out.println("longToDouble");
        Long number = null;
        Double expResult = null;
        Double result = MyUtils.longToDouble(number);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sleepSecond method, of class MyUtils.
     */
    @Test
    public void testSleepSecond() {
        System.out.println("sleepSecond");
        int second = 0;
        MyUtils.sleepSecond(second);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isNull method, of class MyUtils.
     */
    @Test
    public void testIsNull_String() {
        System.out.println("isNull");
        String input = "";
        boolean expResult = false;
        boolean result = MyUtils.isNull(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isNull method, of class MyUtils.
     */
    @Test
    public void testIsNull_Object() {
        System.out.println("isNull");
        Object input = null;
        boolean expResult = false;
        boolean result = MyUtils.isNull(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumber method, of class MyUtils.
     */
    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
        String input = "";
        String expResult = "";
        String result = MyUtils.getNumber(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBoolean method, of class MyUtils.
     */
    @Test
    public void testGetBoolean_String_boolean() {
        System.out.println("getBoolean");
        String input = "";
        boolean defaultVal = false;
        boolean expResult = false;
        boolean result = MyUtils.getBoolean(input, defaultVal);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBoolean method, of class MyUtils.
     */
    @Test
    public void testGetBoolean_int() {
        System.out.println("getBoolean");
        int aInt = 0;
        boolean expResult = false;
        boolean result = MyUtils.getBoolean(aInt);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validString method, of class MyUtils.
     */
    @Test
    public void testValidString() {
        System.out.println("validString");
        String input = "";
        String expResult = "";
        String result = MyUtils.validString(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of string2Int method, of class MyUtils.
     */
    @Test
    public void testString2Int_String() {
        System.out.println("string2Int");
        String input = "";
        int expResult = 0;
        int result = MyUtils.string2Int(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of string2Int method, of class MyUtils.
     */
    @Test
    public void testString2Int_String_int() {
        System.out.println("string2Int");
        String input = "";
        int defaultVal = 0;
        int expResult = 0;
        int result = MyUtils.string2Int(input, defaultVal);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of string2Long method, of class MyUtils.
     */
    @Test
    public void testString2Long_String() {
        System.out.println("string2Long");
        String input = "";
        long expResult = 0L;
        long result = MyUtils.string2Long(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of string2Long method, of class MyUtils.
     */
    @Test
    public void testString2Long_String_long() {
        System.out.println("string2Long");
        String input = "";
        long defaultVal = 0L;
        long expResult = 0L;
        long result = MyUtils.string2Long(input, defaultVal);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of string2Double method, of class MyUtils.
     */
    @Test
    public void testString2Double_String() {
        System.out.println("string2Double");
        String input = "";
        double expResult = 0.0;
        double result = MyUtils.string2Double(input);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of string2Double method, of class MyUtils.
     */
    @Test
    public void testString2Double_String_double() {
        System.out.println("string2Double");
        String input = "";
        double defaultVal = 0.0;
        double expResult = 0.0;
        double result = MyUtils.string2Double(input, defaultVal);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
