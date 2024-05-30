/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class MyCookies {

    private static Logger logger = LoggerFactory.getLogger(MyCookies.class);
    static Map theCookies = new HashMap();

    /**
     * Send the Hashtable (theCookies) as cookies, and write them to the
     * specified URLconnection
     *
     * @param urlConn The connection to write the cookies to.
     * @param printCookies Print or not the action taken.
     *
     * @return The urlConn with the all the cookies in it.
     */
    public URLConnection writeCookies(URLConnection urlConn, boolean printCookies) {
        String cookieString = "";
        Set keys = theCookies.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            cookieString += key + "=" + theCookies.get(key);
            if (iterator.hasNext()) {
                cookieString += "; ";
            }
        }
        urlConn.setRequestProperty("Cookie", cookieString);
        if (printCookies) {
            logger.debug("Wrote cookies:\n   " + cookieString);
        }
        return urlConn;
    }

    /**
     * Read cookies from a specified URLConnection, and insert them to the
     * Hashtable The hashtable represents the Cookies.
     *
     * @param urlConn the connection to read from
     * @param printCookies Print the cookies or not, for debugging
     * @param reset Clean the Hashtable or not
     */
    public void readCookies(URLConnection urlConn, boolean printCookies,
            boolean reset) {
        if (reset) {
            theCookies.clear();
        }
        int i = 1;
        String hdrKey;
        String hdrString;
        String aCookie;
        while ((hdrKey = urlConn.getHeaderFieldKey(i)) != null) {
            if (hdrKey.equals("Set-Cookie")) {
                hdrString = urlConn.getHeaderField(i);
                StringTokenizer st = new StringTokenizer(hdrString, ",");
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    aCookie = s.substring(0, s.indexOf(";"));
                    // aCookie = hdrString.substring(0, s.indexOf(";"));
                    int j = aCookie.indexOf("=");
                    if (j != -1) {
                        if (!theCookies.containsKey(aCookie.substring(0, j))) {
                            // if the Cookie do not already exist then when keep it,
                            // you may want to add some logic to update 
                            // the stored Cookie instead. thanks to rwhelan
                            theCookies.put(aCookie.substring(0, j), aCookie.substring(j + 1));
                            if (printCookies) {
                                logger.debug("Reading Key: "
                                        + aCookie.substring(0, j));
                                logger.debug("        Val: "
                                        + aCookie.substring(j + 1));
                            }
                        }
                    }
                }
            }
            i++;
        }
    }

    /**
     * Display all the cookies currently in the HashTable
     *
     */
    public void viewAllCookies() {
        logger.debug("All Cookies are:");
        Set keys = theCookies.keySet();
        Iterator<String> iterator = keys.iterator();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            logger.debug("   " + key + "="
                    + theCookies.get(key));
        }
    }

    /**
     * Display the current cookies in the URLConnection, searching for the:
     * "Cookie" header
     *
     * This is Valid only after a writeCookies operation.
     *
     * @param urlConn The URL to print the associates cookies in.
     */
    public void viewURLCookies(URLConnection urlConn) {
        System.out.print("Cookies in this URLConnection are:\n   ");
        logger.debug(urlConn.getRequestProperty("Cookie"));
    }

    /**
     * Add a specific cookie, by hand, to the HastTable of the Cookies
     *
     * @param _key The Key/Name of the Cookie
     * @param _val The Calue of the Cookie
     * @param printCookies Print or not the result
     */
    public void addCookie(String _key, String _val, boolean printCookies) {
        if (!theCookies.containsKey(_key)) {
            theCookies.put(_key, _val);
            if (printCookies) {
                logger.debug("Adding Cookie: ");
                logger.debug("   " + _key + " = " + _val);
            }
        }
    }

    /**
     * Cookie http
     *
     * @param request
     * @param name
     * @return
     */
    public static String readCookie(HttpServletRequest request, String name) {
        String val = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie oneCookie : cookies) {
                if (oneCookie.getName().equals(name)) {
                    val = oneCookie.getValue();
                }
            }
        }
        return val;
    }

    public static void writeCookie(HttpServletResponse response, String name, String val, int liveHour) {
        Cookie cookie = new Cookie(name, val);
        cookie.setMaxAge(liveHour * 60 * 60);
        response.addCookie(cookie);
    }

    /*
     public void readCookie(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

     response.setContentType("text/html");
     PrintWriter out = response.getWriter();
     out.println("<HTML>");
     out.println("<HEAD>");
     out.println("<TITLE>");
     out.println("A Web Page");
     out.println("</TITLE>");
     out.println("</HEAD>");
     out.println("<BODY");

     Cookie[] cookies = request.getCookies();
     boolean foundCookie = false;
     for (Cookie oneCookie : cookies) {
     if (oneCookie.getName().equals("color")) {
     out.println("bgcolor = " + oneCookie.getValue());
     foundCookie = true;
     }
     }

     if (!foundCookie) {
     Cookie cookie1 = new Cookie("color", "cyan");
     cookie1.setMaxAge(24 * 60 * 60);
     response.addCookie(cookie1);
     }

     out.println(">");
     out.println("<H1>Setting and Reading Cookies</H1>");
     out.println("This page will set its background color using a cookie when reloaded.");
     out.println("</BODY>");
     out.println("</HTML>");
     }
     */
}
