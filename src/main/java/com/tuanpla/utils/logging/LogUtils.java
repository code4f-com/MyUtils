/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tuanpla.utils.logging;

import com.tuanpla.utils.common.ConsoleColors;
import com.tuanpla.utils.config.PublicConfig;
import static com.tuanpla.utils.config.PublicConfig.PROJECT_NAME;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tuanp
 */
public class LogUtils {

    /**
     * alway print message input
     *
     * @param input
     */
    public static void out(Object input) {
        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        System.out.println(ConsoleColors.GREEN + PROJECT_NAME + ": " + ConsoleColors.BLUE + " INFO " + ConsoleColors.GREEN + className + ".java [d." + lineNumber + "] " + input + ConsoleColors.RESET);
    }

    /**
     * print message to console When DE_BUG = true in PublicConfig
     *
     * @param input
     */
    public static void debug(Object input) {
        if (PublicConfig.DE_BUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
            System.out.println(ConsoleColors.PURPLE + PROJECT_NAME + ":" + ConsoleColors.BLUE + " DEBUG " + ConsoleColors.PURPLE + className + ".java [d." + lineNumber + "] " + input + ConsoleColors.RESET);
        }
    }

    public static void error(Object input) {
        if (PublicConfig.DE_BUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
            System.err.println(ConsoleColors.RED + PROJECT_NAME + ": " + " ERROR " + className + ".java [d." + lineNumber + "] " + input + ConsoleColors.RESET);
        }
    }

    public static void error(Exception ex) {
        String msg = getLogMessage(ex);
        if (PublicConfig.DE_BUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
            System.err.println(ConsoleColors.RED + PROJECT_NAME + ": " + " ERROR " + className + ".java [d." + lineNumber + "] " + msg + ConsoleColors.RESET);
        }
    }

    public static void logError(Logger logger, Exception ex) {
        String msg = getLogMessage(ex);
        logger.error(msg);
    }

    public static void logInfo(Logger logger, Exception ex) {
        String msg = getLogMessage(ex);
        logger.info(msg);
    }

    public static String getLogMessage(Exception ex) {
        String str = "message: " + ex.getMessage() + System.lineSeparator();
        str += "Detail =>" + System.lineSeparator();
        StackTraceElement[] trace = ex.getStackTrace();
        for (StackTraceElement trace1 : trace) {
            str += trace1 + System.lineSeparator();
        }
        return str;
    }
}
