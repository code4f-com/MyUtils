/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.file;

import com.tuanpla.utils.common.MyString;
import com.tuanpla.utils.http.HttpUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 *
 * @author tuanp
 */
public class FCKUtils {

    public static String ARTICLES_IMG_THUMB_ALIAS;
    public static String ARTICLES_IMG_CACHE_ALIAS;
    // Noi cache Anh cua Content va Content
    public static int[] WIDTH_IMAGE_THUMBNAIL;
    public static int WIDTH_IMAGE_IN_CONTENT;

    public static String FCKProcessImage(String content, String title_url) {
        if (content == null || content.equals("")) {
            return content;
        }
        String strFirst;
        String strLast;
        ArrayList<String> listImgTag = new ArrayList<>();
        int posImg = 0;
        try {
            int index = -1;
            while ((index = content.indexOf("<img")) > 0) {
                int lastIndex = -1;
                String striamge = "";
                // Phan truoc the <img
                strFirst = content.substring(0, index);
                // Phan tu the <img tro di
                strLast = content.substring(index);
//                debug("phan con lai= "+strLast);
                lastIndex = strLast.indexOf(">");
                striamge = strLast.substring(0, lastIndex + 1);
//                debug(striamge);
                // Phan con lai sau khi da loai the <img...>
                strLast = strLast.substring(striamge.length());
//                debug(strLast);
                // Duong dan anh lay ra de xu ly
                listImgTag.add(striamge);
                // Tra lai str da lay duoc link anh
                content = strFirst + "##" + posImg + "##" + strLast;
//                debug("str="+str);
//                debug("--------------------------------");
                posImg++;
                index = -1;
            }
            /**
             * < XU LY CAC LINK ANH
             */
            int indexTem = 0;
            ArrayList<String> contentLink = ImageProcessCache(listImgTag, title_url);
            for (String oneLink : contentLink) {
                String imgStr = "<img alt='" + title_url + "' title ='" + title_url + "' class=\"photo\" src=\"" + oneLink + "\" />";
                content = MyString.replaceString(content, "##" + indexTem + "##", imgStr);
                indexTem++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String FCKProcessImageDisplay(String content, String title_url) {
        if (content == null || content.equals("")) {
            return content;
        }
        String strFirst;
        String strLast;
        ArrayList<String> listImgTag = new ArrayList<>();
        int posImg = 0;
        try {
            int index = -1;
            while ((index = content.indexOf("<img")) > 0) {
                int lastIndex = -1;
                String striamge = "";
                // Phan truoc the <img
                strFirst = content.substring(0, index);
                // Phan tu the <img tro di
                strLast = content.substring(index);
//                debug("phan con lai= "+strLast);
                lastIndex = strLast.indexOf(">");
                striamge = strLast.substring(0, lastIndex + 1);
//                debug(striamge);
                // Phan con lai sau khi da loai the <img...>
                strLast = strLast.substring(striamge.length());
//                debug(strLast);
                // Duong dan anh lay ra de xu ly
                listImgTag.add(striamge);
                // Tra lai str da lay duoc link anh
                content = strFirst + "##" + posImg + "##" + strLast;
//                debug("str="+str);
//                debug("--------------------------------");
                posImg++;
                index = -1;
            }
            int indexTem = 0;
            for (String urlImage : listImgTag) {
                urlImage = HttpUtil.getUrlFromImageTag(urlImage);
                String realLink = "";
                if (urlImage.contains("myname.vn")) {
                    realLink = urlImage;
                } else {
                    realLink = "http://myname.vn" + urlImage;
                }
                String imgStr = "<img alt='" + title_url + "' title ='" + title_url + "' class=\"photo\" src=\"" + realLink + "\" />";
                content = MyString.replaceString(content, "##" + indexTem + "##", imgStr);
                indexTem++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String validStringFCK(String content, String title_url) {
        if (content != null) {
            content = content.trim();
        } else {
            content = "";
        }
        // Xu lai Title cho ImageName
        title_url = MyString.getStringURL(title_url);
        content = FCKProcessImage(content, title_url);
        return content;
    }

    /**
     * Xu ly lay anh ve Cache va tra lai List duong dan thuc theo Host cua Image
     *
     * @param listImgTag
     * @param title_url
     * @return
     */
    public static ArrayList<String> ImageProcessCache(ArrayList<String> listImgTag, String title_url) {
        ArrayList<String> allRealLink = new ArrayList<>();
        try {
            int i = 1;
            for (String urlImage : listImgTag) {
                // Duong dan anh trong the
                urlImage = HttpUtil.getUrlFromImageTag(urlImage);
                if (urlImage.contains(ARTICLES_IMG_CACHE_ALIAS)) {
                    allRealLink.add(urlImage);
                } else {
                    String _alias = ARTICLES_IMG_CACHE_ALIAS + "/" + title_url + "_" + i + "." + getExtentionImageRequest(urlImage);
                    // Duong dan that
                    String real_path = ARTICLES_IMG_CACHE_ALIAS + "/" + title_url + "_" + i + "." + getExtentionImageRequest(urlImage);
//                    debug("REAL PATH:" + real_path);
                    cacheImageRequest(urlImage, real_path, true, WIDTH_IMAGE_IN_CONTENT);
                    //--------- Cho nay vi insert vao trong Web server nen ko con cai URL host nua
//                    String url_by_host = MyContext.URL_HOST_ALIAS_IMAGE + pathSave;
                    allRealLink.add(_alias);
                }
                i++;
            }
        } catch (Exception e) {
        }
        return allRealLink;
    }

    private static String getExtentionImageRequest(String urlImage) {
        String ext = "jpg";
        if (urlImage == null) {
            return ext;
        }
        int index = urlImage.lastIndexOf(".");
        if (index != -1) {
            ext = urlImage.substring(index + 1);
        } else {
            return ext;
        }
        return ext;
    }

    public static boolean cacheImageRequest(String urlImage, String realPath, boolean resize, int width_max) {
        boolean flag = false;
        try {
            URL urlimg = new URL(urlImage);
            URLConnection ucconnimg = urlimg.openConnection();
            InputStream ipstimg = ucconnimg.getInputStream();
            if (resize) {
                // Thay doi Kich Thuoc Anh Lay Ve                
                //TODO
                FileUtils.resizeMaxWithWriteImg(ipstimg, width_max, realPath, "jpg");
            } else {
                byte[] imgCache = DataConvert.InputStream2Bytes(ipstimg);
                if (imgCache != null && imgCache.length > 0) {
                    try (FileOutputStream file = new FileOutputStream(realPath)) {
                        file.write(imgCache);
                        file.flush();
                    }
                    flag = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
