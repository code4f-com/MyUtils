/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.http;

import com.tuanpla.utils.string.MyString;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {

    public static String getRanUserAgent() {
        String userAgent = "Windows NT 5.1) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Safari/534.24";
        try {
            userAgent = USER_AGENT[buildRandomUserAgent()];
        } catch (Exception e) {
        }

        return userAgent;
    }

    private static int buildRandomUserAgent() {
        int ran = 0;
        while (true) {
            ran = (int) (Math.random() * 10);
            if (ran < USER_AGENT.length) {
                break;
            }
        }
        return ran;
    }
    private static String[] USER_AGENT = {
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 1.1.4322; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E; InfoPath.3)",
        "Opera/9.80 (Windows NT 5.1; U; Edition Campaign 04; en) Presto/2.7.62 Version/11.01",
        "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Safari/534.24",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:107.0) Gecko/20100101 Firefox/107.0",
        "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13",
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)",
        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)",
        "msnbot/1.1 (+http://search.msn.com/msnbot.htm)",
        "Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)",
        "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
    };

    public static String validStringUserInput(String sStr) {

        if (sStr == null) {
            sStr = "";
        }
        sStr = sStr.replaceAll("&", "&amp;");
        sStr = sStr.replaceAll("\"", "&quot;");
        sStr = sStr.replaceAll("'", "&apos;");
        sStr = sStr.replaceAll("<", "&lt;");
        sStr = sStr.replaceAll(">", "&gt;");
        sStr = sStr.replaceAll("&lt;br&gt;", "<br/>");
        sStr = sStr.replaceAll("&lt;br/&gt;", "<br/>");
        sStr = sStr.replaceAll("&lt;p&gt;", "<p>");
        sStr = sStr.replaceAll("&lt;/p&gt;", "</p>");
        sStr = sStr.replaceAll("&lt;hr&gt;", "<hr/>");
        sStr = sStr.replaceAll("&lt;hr/&gt;", "<hr/>");
        sStr = sStr.replaceAll("\n", "<br/>");
        return sStr;
    }

    public static String convertTitle(String input) {
        if (input == null) {
            return null;
        }
        input = MyString.convert2NoSign(input);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static String validTitle(String str) {
        if (str == null) {
            str = "";
        }
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("'", "&apos;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        return str.trim();
    }

    public static String tag2String(String[] tag) {
        String tem = "";
        for (String one : tag) {
            tem += one + ",";
        }
        if (tem.endsWith(",")) {
            tem = tem.substring(0, tem.length() - 1);
        }
        return tem;
    }

    public static String[] tag2Nosign(String[] tag) {
        if (tag == null || tag.length == 0) {
            return null;
        } else {
            for (int i = 0; i < tag.length; i++) {
                tag[i] = convertTitle(tag[i]);
            }
        }
        return tag;
    }

    public static String html2text(String contentsHtml) throws Exception {
        StringBuilder buff = new StringBuilder(contentsHtml.length());
        char c;
        boolean intag = false;
        for (int i = 0; i < contentsHtml.length(); i++) {
            c = contentsHtml.charAt(i);
            if ((c == '<') && ((contentsHtml.charAt(i + 1) == 'a') || (contentsHtml.charAt(i + 1) == 'A'))) {
                intag = false;
            } else if ((c == '<') && (contentsHtml.charAt(i + 1) == '/') && ((contentsHtml.charAt(i + 2) == 'a') || (contentsHtml.charAt(i + 2) == 'A'))) {
                intag = false;
            } else if (c == '<') {
                intag = true;
            }
            if (!intag) {
                buff.append(c);
            }
            if (c == '>') {
                intag = false;
            }
        }
        return buff.toString();
    }

    /**
     * parse String ve theo dinh dang plain text(chi la cac the don thuan ko co
     * css) param return
     *
     * @param contentsHtml
     * @return
     * @throws java.lang.Exception
     */
    public static String htmlPlainTextAll(String contentsHtml) throws Exception {
        String[][] complie = {
            {"<!--([^>])*-->", ""}, //<comment>
            {"<( )*table([^>])*>", ""}, //<table>
            {"<( )*/table([^>])*>", ""}, //</table>
            {"font-face([^>])*; }", ""}, //</table>

            {"<([^>])*style([^>])*>", "<style>"}, //  <style  >
            {"<style>([^>])*></style>", ""}, //  <style  >

            {"<( )*tbody([^>])*>", ""}, //<tbody>
            {"<( )*/tbody([^>])*>", ""}, //</tbody>

            {"<( )*h\\d([^>])*>", ""}, //<h?>
            {"<( )*/h\\d([^>])*>", "<br/>"}, //</h?>

            // {"<( )*tr([^>])*>","<p>"},      //<tr>   Thay the bang <p>
            {"<( )*tr([^>])*>", ""}, //<tr>
            // {"<( )*/tr([^>])*>","</p>"},    //</tr>  Thay the bang </p>
            {"<( )*/tr([^>])*>", "<br/>"}, //</tr>

            {"<( )*td([^>])*>", ""}, //<td>
            {"<( )*/td([^>])*>", " "}, //</td>

            // {"<( )*div([^>])*>","<div>"},   //<div>
            {"<( )*div([^>])*>", ""}, //<div>
            {"<( )*/div([^>])*>", "<br/>"}, //<div>

            {"<( )*embed([^>])*>", ""}, //<embed>
            {"<( )*/embed([^>])*>", " "}, //</embed>

            {"<( )*center([^>])*>", ""}, //<center>
            {"<( )*/center([^>])*>", " "}, //</center>
            //  {"<( )*p([^>])*>","<p>"},       //<p>
            {"<( )*p([^>])*>", ""}, //<p>
            {"<( )*/p([^>])*>", "<br/>"}, //<p>

            {"<o:p([^>])*>", ""}, //
            {"</o:p>", "<br/>"}, //
            {"<[a-z]:([^>])*>", ""}, //         The La tu Define Vidu dantri se co cac loai the kieu nay
            {"</[a-z]:([^>])*>", ""}, //    Dong The La tu Define Vidu dantri se co cac loai the kieu nay

            {"<( )*blockquote([^>])*>", ""}, //<blockquote>
            {"<( )*/blockquote([^>])*>", " "}, //</blockquote>

            {"<( )*span([^>])*>", ""}, //<span>
            {"<( )*/span([^>])*>", " "}, //</span>

            {"<( )*font([^>])*>", ""}, //<font>
            {"<( )*/font([^>])*>", " "}, //</font>

            {"<( )*em([^>])*>", ""}, //<font>
            {"<( )*/em([^>])*>", " "}, //</font>
            {"<( )*li([^>])*>", ""}, //<li>
            {"<( )*/li([^>])*>", ""}, //</li>

            {"<( )*st([^>])*>", " "}, //</st>
            {"<( )*/st([^>])*>", " "}, //</st>

            //  {"<( )*(.?)*( )*class=([^>])*>", ""}, //<p  class=  > </>
            //{"<( )*br([^>])*>",""},               //<br>
            {"<( )*br([^>])*>", "<br/>"}, //<br>
            {"<( )*br/([^>])*>(\\s)*<( )*br/([^>])*>", "<br/>"},
            //{"<( )*br/([^>])*>( )*<( )*br/([^>])*>","<br/>"},

            {"<( )*a([^>])*>", ""}, //<a>
            {"<( )*/a([^>])*>", " "}, //</a>
            {"<( )*input([^>])*>", " "}, //<input>

            {"<( )*strong([^>])*><br/>", "<strong>"}, //
            {"<strong>( )*</strong>", ""}, //</strong>
            {"<br/>( )*<br/>", "<br/>"},
            {"<( )*b ([^>])*>", "<b>"},
            {"<b>", ""},
            {"</b>", ""},
            //  {"<( )*b([^>])*><br/>", "<b>"}, //
            {"<( )*i ([^>])*>", "<i>"}, //
            {"<( )*em([^>])*><br/>", "<em>"}, //
            {"<( )*img.+src=\"(.?)*\"([^>])*>", ""} //<img>
        };
        for (int i = 0; i < complie.length; i++) {
            contentsHtml = contentsHtml.replaceAll(complie[i][0], complie[i][1]);
        }

//        String sImgcompile = "<( )*img.+src=\"(.?)*\"([^>])*>";
//        if (!complie.equals("")) {
//            Pattern pattern = Pattern.compile(sImgcompile);
//            Matcher matcher = pattern.matcher(contentsHtml);
//            int idx = 0;
//            String link = "";
//            while (matcher.find()) {
//                String s4 = matcher.group(0);
////                Tool.debug("-->"+s4);
//                idx = s4.indexOf("src=\"");
////                Tool.debug("idx: "+idx);
//                if (idx > 0) {
//                    link = s4.substring(idx + 5); //+5 vi idx la vi tri cua ky tu s, nen pai tien 5 phan tu nua theo do dai cua thang src="
//                    link = link.substring(0, link.indexOf("\""));
//                    link = "<div align='center'><img class=\"pnews\" src=\"" + link + "\" alt =\"Bạn đợi chút...\"/></div>";
////                    Tool.debug("link replace-->:"+link);
//                    contentsHtml = contentsHtml.replace(s4, link);
//                }
//            }
//        }
        return contentsHtml;
    }

    /**
     * Convert 1 String thanh HTML ko co http://xxx param s return
     *
     * @param s
     * @return
     */
    public static String text2HtmlnoLink(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    builder.append("&nbsp;");
                    previousWasASpace = false;
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\n':
                    builder.append("<br>");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("&nbsp; &nbsp; &nbsp;");
                    break;
                default:
                    if (c < 128) {
                        builder.append(c);
                    } else {
                        builder.append("&#").append((int) c).append(";");
                    }
            }
        }
        return builder.toString();
    }

    /**
     * *
     * Convert 1 String thanh HTML xu ly http://xxx thanh the
     * <code><a ></a></code> param s return
     *
     * @param s
     * @return
     */
    public static String txtToHtml(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    builder.append("&nbsp;");
                    previousWasASpace = false;
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\n':
                    builder.append("<br>");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("&nbsp; &nbsp; &nbsp;");
                    break;
                default:
//                    builder.append(c);
                    if (c < 128) {
                        builder.append(c);
                    } else {
                        builder.append("&#").append((int) c).append(";");
                    }

            }
        }
        String converted = builder.toString();
        String str = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>?«»“”‘’]))";
        Pattern patt = Pattern.compile(str);
        Matcher matcher = patt.matcher(converted);
        converted = matcher.replaceAll("<a href=\"$1\">$1</a>");
        return converted;
    }

    public static String jsonToAttr(String input) {
        if (input == null || input.equals("")) {
            return "";
        }
        input = input.trim();
        input = input.replaceAll("'", "&#39;");
        input = input.replaceAll("\"", "&quot;");
        input = input.replaceAll("&", "&amp;");
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");

        return input;
    }

    public static String htmlCode2Vn(String input) {
        if (input == null || input.equals("")) {
            return "";
        }
        input = input.replaceAll("&Agrave;", "À");
        input = input.replaceAll("&agrave;", "à");
        input = input.replaceAll("&#258;", "Ă");
        input = input.replaceAll("&#259;", "ă");
        input = input.replaceAll("&Aacute;", "Á");
        input = input.replaceAll("&aacute;", "á");
        input = input.replaceAll("&Acirc;", "Â");
        input = input.replaceAll("&acirc;", "â");
        input = input.replaceAll("&Atilde;", "Ã");
        input = input.replaceAll("&atilde;", "ã");
        //--
        input = input.replaceAll("&Egrave;", "È");
        input = input.replaceAll("&egrave;", "è");
        input = input.replaceAll("&Eacute;", "É");
        input = input.replaceAll("&eacute;", "é");
        input = input.replaceAll("&Ecirc;", "Ê");
        input = input.replaceAll("&ecirc;", "ê");
        //--
        input = input.replaceAll("&Ograve;", "Ò");
        input = input.replaceAll("&ograve;", "ò");
        input = input.replaceAll("&Oacute;", "Ó");
        input = input.replaceAll("&oacute;", "ó");
        input = input.replaceAll("&Otilde;", "Õ");
        input = input.replaceAll("&otilde;", "õ");
        input = input.replaceAll("&Ocirc;", "Ô");
        input = input.replaceAll("&ocirc;", "ô");
        //---
        input = input.replaceAll("&Ugrave;", "Ù");
        input = input.replaceAll("&ugrave;", "ù");
        input = input.replaceAll("&Uacute;", "Ú");
        input = input.replaceAll("&uacute;", "ú");
        input = input.replaceAll("&#360;", "Ũ");
        input = input.replaceAll("&#361;", "ũ");
        //--
        input = input.replaceAll("&Igrave;", "Ì");
        input = input.replaceAll("&igrave;", "ì");
        input = input.replaceAll("&Iacute;", "Í");
        input = input.replaceAll("&iacute;", "í");
        input = input.replaceAll("&#296;", "Ĩ");
        input = input.replaceAll("&#297;", "ĩ");
        //--
        input = input.replaceAll("&#272;", "Đ");
        input = input.replaceAll("&#273;", "đ");
        input = input.replaceAll("&nbsp;", " ");
        return input;
    }

    public static String removeElement(String input, String patten) {
        if (input == null) {
            return "";
        } else {
            Pattern pattern = Pattern.compile(patten);
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String str = matcher.group(0);
                input = MyString.replaceString(input, str, "");
            }
            return input;
        }

    }

    /**
     * Remove Element and content inner html Tag by Class
     *
     * @param input
     * @param tag
     * @param clas
     * @return
     */
    public static String removeElement(String input, String tag, String clas) {
        if (input == null) {
            return "";
        } else {
            input = input.replaceAll("<( )*" + tag + ".+class=\"(" + clas + ")\"([^>])*>([\\s\\S.]*)</" + tag + ">", "");
            return input;
        }
    }

    /**
     * Remove Attribute HTML
     *
     * @param htmlFragment
     * @param attributesToRemove
     * @return
     */
    public static String cleanHtmlFragment(String htmlFragment, String attributesToRemove) {
        return htmlFragment.replaceAll("\\s+(?:" + attributesToRemove + ")\\s*=\\s*\"[^\"]*\"", "");
    }
}
