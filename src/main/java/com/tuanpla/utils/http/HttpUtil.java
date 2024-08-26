/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.http;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6;
import com.github.jgonian.ipmath.Ipv6Range;
import com.tuanpla.utils.common.ConsoleColors;
import com.tuanpla.utils.common.LogUtils;
import com.tuanpla.utils.common.MyString;
import com.tuanpla.utils.common.Nullable;
import com.tuanpla.utils.config.HttpConstants;
import static com.tuanpla.utils.config.PublicConfig.PROJECT_NAME;
import com.tuanpla.utils.json.GsonUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String getUrlFromImageTag(String imageTag) {
        try {
            imageTag = imageTag.replaceAll(" ", "");
            imageTag = imageTag.substring(imageTag.indexOf("src=\"") + 5);
            imageTag = imageTag.substring(0, imageTag.indexOf("\""));
        } catch (Exception e) {
        }
        return imageTag;
    }

    public static String getDomainName(String url) throws MalformedURLException {
        String host = "";
        try {
            if (!url.startsWith("http") && !url.startsWith("https")) {
                url = "http://" + url;
            }
            URL netUrl = new URL(url);
            host = netUrl.getHost();

            if (host.startsWith("www")) {
                host = host.substring("www".length() + 1);
            }
        } catch (Exception e) {
            logger.error("[ERROR getDomainName] ==> " + url);
        }
        return host;
    }

    public static String loadUrl(String urlStr) {
        String t = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(20000);
            try (InputStream in = http.getInputStream()) {
                t = MyString.convertStreamToString(in);
            }
            http.disconnect();
        } catch (Exception e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return t;
    }

    public static boolean existsFileUrl(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
//            HttpURLConnection.setInstanceFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            logger.error(LogUtils.getLogMessage(e));
            return false;
        }
    }

    public static boolean isMultipart(HttpServletRequest request) {
        boolean isMultipart = Boolean.FALSE;
        String contentType = request.getContentType();
        if (contentType != null && contentType.startsWith(HttpConstants.MULTIPART_FORM_DATA_VALUE)
                && contentType.contains("boundary=")) {
            isMultipart = true;
        }
        return isMultipart;
    }

    public static void debugParam(HttpServletRequest request) {
        logger.debug("--------debugParam--------");
        boolean isMultipart = isMultipart(request);
        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        if (!isMultipart) {
            Enumeration<String> allParam = request.getParameterNames();
            while (allParam.hasMoreElements()) {
                String oneParam = allParam.nextElement();
                System.out.println(ConsoleColors.MAGENTA + PROJECT_NAME + ":" + ConsoleColors.BLUE + " DEBUG " + ConsoleColors.MAGENTA + className + ".java [d" + lineNumber + "] " + (oneParam + ":" + request.getParameter(oneParam)) + ConsoleColors.RESET);
//                logger.debug(oneParam + ":" + request.getParameter(oneParam));
            }
        } else {
            logger.debug("form isMultipart");
            try {
//                HttpServletMultipartRequest mreq = new HttpServletMultipartRequest(request);
                Enumeration<String> allParam = request.getParameterNames();
                while (allParam.hasMoreElements()) {
                    String oneParam = allParam.nextElement();
                    System.out.println(ConsoleColors.MAGENTA + PROJECT_NAME + ":" + ConsoleColors.BLUE + " DEBUG " + ConsoleColors.MAGENTA + className + ".java [d" + lineNumber + "] " + (oneParam + ":" + request.getParameter(oneParam)) + ConsoleColors.RESET);
//                logger.debug(oneParam + ":" + request.getParameter(oneParam));
                }
            } catch (Exception e) {
                logger.error(LogUtils.getLogMessage(e));
            }
        }
        logger.debug("--------End debugParam--------");
    }

    public static void debugAttribute(HttpServletRequest request) {
        Enumeration<String> attr = request.getAttributeNames();
        logger.debug("==> debugAttribute -- Value");
        while (attr != null && attr.hasMoreElements()) {
            String k = attr.nextElement();
            logger.debug("key:" + k + "| val:" + request.getAttribute(k));
        }
    }

    public static void debugHeader(HttpServletRequest request) {
        Enumeration<String> attr = request.getHeaderNames();
        logger.debug("==> debugHeader -- Value");
        while (attr != null && attr.hasMoreElements()) {
            String k = attr.nextElement();
            logger.debug("key:" + k + "| val:" + request.getHeader(k));
        }
    }

    public static void debugCookies(HttpServletRequest request) {
        Cookie[] ck = request.getCookies();
        logger.debug("==> debugCookies -- Value");
        if (ck != null) {
            for (Cookie one : ck) {
                logger.debug("Cookie GS:" + GsonUtil.toJson(one));
            }
        }
    }

    public static String getHeader(HttpServletRequest request, String param) {
        String result;
        try {
            result = request.getHeader(param).trim();
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    public static String extractIndex(String param) {
        String str = "";
        try {
            int fisrtIdex = param.indexOf("[");
            int lastIdex = param.lastIndexOf("]");
            str = param.substring(fisrtIdex + 1, lastIdex);
        } catch (Exception e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return str;
    }

    public static String extractIndex(String param, String reg) {
        String index = "";
        try {
            String[] arr = param.split(reg);
            index = arr[1];
        } catch (Exception e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return index;
    }

    public static int getInt(HttpServletRequest request, String param) {
        int result;
        try {
            result = Integer.parseInt(request.getParameter(param).trim());
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static int getInt(HttpServletRequest request, String param, int defaultVal) {
        int result;
        try {
            result = Integer.parseInt(request.getParameter(param).trim());
        } catch (Exception e) {
            result = defaultVal;
        }
        return result;
    }

    public static boolean getBoolean(HttpServletRequest request, String param) {
        boolean result;
        try {
            String str = request.getParameter(param).trim();
            result = str != null && (str.equals("1") || str.equals("true") || str.equals("on"));
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static boolean getBoolean(HttpServletRequest request, String param, boolean defaultVal) {
        boolean result;
        try {
            String str = request.getParameter(param).trim();
            result = str != null && (str.equals("1") || str.equals("true") || str.equals("on"));
        } catch (Exception e) {
            result = defaultVal;
        }
        return result;
    }

    public static long getLong(HttpServletRequest request, String param) {
        long result;
        try {
            result = Long.parseLong(request.getParameter(param).trim());
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static Long getLong(HttpServletRequest request, String param, @Nullable Long defaultVal) {
        Long result;
        try {
            result = Long.valueOf(request.getParameter(param).trim());
        } catch (Exception e) {
            result = defaultVal;
        }
        return result;
    }

    public static double getDouble(HttpServletRequest request, String param) {
        double result;
        try {
            result = Double.parseDouble(request.getParameter(param).trim());
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static Double getDouble(HttpServletRequest request, String param, @Nullable Double defaultVal) {
        Double result;
        try {
            result = Double.valueOf(request.getParameter(param).trim());
        } catch (Exception e) {
            result = defaultVal;
        }
        return result;
    }

    public static String getString(HttpServletRequest request, String param) {
        String result;
        try {
            result = request.getParameter(param).trim();
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    public static String getString(HttpServletRequest request, String param, @Nullable String defaultVal) {
        String result;
        try {
            result = request.getParameter(param).trim();
        } catch (Exception e) {
            result = defaultVal;
        }
        return result;
    }

    public static Float getFloat(HttpServletRequest request, String param) {
        float result;
        try {
            result = Float.parseFloat(request.getParameter(param).trim());
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static Float getFloat(HttpServletRequest request, String param, @Nullable Float defaultVal) {
        Float result;
        try {
            result = Float.valueOf(request.getParameter(param).trim());
        } catch (Exception e) {
            result = defaultVal;
        }
        return result;
    }

    public static String[] getArrValue(HttpServletRequest request, String param) {
        String[] data;
        try {
            data = request.getParameterValues(param);
        } catch (Exception e) {
            data = new String[0];
        }
        return data;
    }

    public static List<String> getStringArrPrefix(HttpServletRequest request, String prefix) {
        List<String> data = new ArrayList<>();
        try {
            data = Collections
                    .list(request.getParameterNames())
                    .stream()
                    .filter(paramName -> paramName.startsWith(prefix))
                    .map(request::getParameter)
                    .collect(Collectors.toList());
        } catch (Exception e) {
        }
        return data;
    }

    //--
    public String getCookie(HttpServletRequest request, String c_name) {
        String value = "";
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(c_name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return value;
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getURI(HttpServletRequest request) {
        String currentURL;
        if (request.getAttribute("jakarta.servlet.forward.request_uri") != null) {
            currentURL = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
        } else {
            currentURL = request.getRequestURI();
        }
        if (currentURL != null && request.getAttribute("jakarta.servlet.include.query_string") != null) {
            currentURL += "?" + request.getQueryString();
        }
        return currentURL;
    }

    public static String getFullURL(HttpServletRequest request) {
        String currentURL;
        String domain = request.getScheme() + "://" + request.getHeader("host");
        if (request.getAttribute("jakarta.servlet.forward.request_uri") != null) {
            currentURL = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
        } else {
            currentURL = request.getRequestURI();
        }
        if (currentURL != null && request.getAttribute("jakarta.servlet.include.query_string") != null) {
            currentURL += "?" + request.getQueryString();
        }
        return domain + currentURL;
    }

    public static String getPageUrl(HttpServletRequest request) {
        String pageURL = getFullURL(request) + "?";
        Enumeration paraList = request.getParameterNames();
        String data = "";
        while (paraList.hasMoreElements()) {
            String paraName = String.valueOf(paraList.nextElement());
            if (!paraName.equalsIgnoreCase("page") && !paraName.equalsIgnoreCase("submit")) {
                logger.debug("paraName:" + paraName + "=" + request.getParameter(paraName));
                data += paraName + "=" + request.getParameter(paraName) + "&amp;";
            }
        }
//        debug("data before Strim: " + data);
        if (data.endsWith("&amp;")) {
            data = data.substring(0, data.length() - 5);
        }
        if (data.startsWith("&amp;")) {
            data = data.substring(5);
        }
        if (data.startsWith("&")) {
            data = data.substring(1);
        }
//        debug("data affter Trim: " + data);
        return pageURL + data + "&";
    }

    public static boolean matchesURI(String uri, String contextPath, String path) {
        boolean result = Boolean.FALSE;
        try {
            path = Pattern.quote(path);
            Pattern p = Pattern.compile("^(" + path + ")$");
            Pattern pc = Pattern.compile("^(" + contextPath + path + ")$");
            Matcher m = p.matcher(uri);
            Matcher mc = pc.matcher(uri);
            result = m.matches() || mc.matches();
        } catch (Exception e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    /**
     *
     * @param ipRang example 192.168.1.1/24
     * @return
     */
    public static String[] toArray(String ipRang) {
        SubnetUtils utils = new SubnetUtils(ipRang);
        String[] allIps = utils.getInfo().getAllAddresses();
        return allIps;
    }

    public static boolean checkIPv4IsInRange(String inputIP, String rangeStartIP, String rangeEndIP) {
        Ipv4 startIPAddress = Ipv4.of(rangeStartIP);
        Ipv4 endIPAddress = Ipv4.of(rangeEndIP);
        Ipv4Range ipRange = Ipv4Range.from(startIPAddress).to(endIPAddress);
        Ipv4 inputIPAddress = Ipv4.of(inputIP);
        return ipRange.contains(inputIPAddress);
    }

    public static boolean checkIPv6IsInRange(String inputIP, String rangeStartIP, String rangeEndIP) {
        Ipv6 startIPAddress = Ipv6.of(rangeStartIP);
        Ipv6 endIPAddress = Ipv6.of(rangeEndIP);
        Ipv6Range ipRange = Ipv6Range.from(startIPAddress).to(endIPAddress);
        Ipv6 inputIPAddress = Ipv6.of(inputIP);
        return ipRange.contains(inputIPAddress);
    }
}
