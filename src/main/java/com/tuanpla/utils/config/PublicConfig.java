/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.config;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author tuanp
 */
public abstract class PublicConfig {

    public static boolean AppRunning = true;
    public static boolean DE_BUG = true;
    public static boolean BUNDLES_MODEL_APP = false;

    public static String PROJECT_NAME = "Unknow Project Name";
    public static String MAIN_LOGGER_NAME = "APP_LOG";
    public static String AUTH_CUS_MD5 = "Customize String for cusEncrypt MD5";

    public static long AUTO_LOAD_LIB_TIME = 30 * 1000;
    public static long DEFAULT_EXPIRE_TIME = 5 * 1000;
    public static int MAX_FILE_SIZE = 10 * 1024 * 1024;

    // FOR FINAL CONSTANT
    public static final String UTF_8 = StandardCharsets.UTF_8.name();
    public static final String UTF_16 = StandardCharsets.UTF_16.name();
    public static final boolean ISDEL = true;
}
