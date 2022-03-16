/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuanpla.utils.json;

/**
 *
 * @author tuanp
 */
public class JacksonUtil {

//    private static ObjectMapper objMapper = new ObjectMapper();
//
//    public static String toJson(Object obj) {
//        String str = "";
//
//        try {
//            str = objMapper.writeValueAsString(obj);
//        } catch (JsonProcessingException e) {
//            logger.error(getLogMessage(e));
//        }
//
//        return str;
//    }
//
//    public static String serializeAsJsonString(Object object) {
//        StringWriter sw = new StringWriter();
//        try {
//            objMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//            objMapper.writeValue(sw, object);
//        } catch (IOException e) {
//            logger.error(Tool.getLogMessage(e));
//        }
//        return sw.toString();
//    }
//
//    public static String serializeAsJsonString(Object object, boolean indent) {
//        StringWriter stringWriter = new StringWriter();
//        try {
//            if (indent == true) {
//                objMapper.enable(SerializationFeature.INDENT_OUTPUT);
//                objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//            }
//            objMapper.writeValue(stringWriter, object);
//        } catch (IOException e) {
//            logger.error(Tool.getLogMessage(e));
//        }
//        return stringWriter.toString();
//    }
//
//    public static <T> T jsonStringToObject(String content, Class<T> clazz) {
//        T result = null;
//        try {
//            result = objMapper.readValue(content, clazz);
//        } catch (JsonProcessingException e) {
//        }
//        return result;
//    }
//
//    @SuppressWarnings("rawtypes")
//    public static <T> T jsonStringToObjectArray(String content) {
//        T result = null;
//        try {
//            result = objMapper.readValue(content, new TypeReference<T>() {
//            });
//        } catch (JsonProcessingException e) {
//        }
//        return result;
//    }
//
//    public static <T> T jsonStringToObjectArray(String content, Class<T> clazz) {
//        T result = null;
//        try {
//            objMapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//            result
//                    = objMapper.readValue(content, objMapper.getTypeFactory().constructCollectionType(List.class,
//                             clazz));
//        } catch (JsonProcessingException e) {
//        }
//        return result;
//    }
}
