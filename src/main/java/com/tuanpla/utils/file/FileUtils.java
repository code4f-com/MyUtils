/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.file;

import com.tuanpla.utils.logging.LogUtils;
import com.tuanpla.utils.string.StringUtils;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class FileUtils {

    static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    public static long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     *
     * @param arrbyte
     * @param full_path
     * @return
     */
    public static boolean writeFileToDisk(byte[] arrbyte, String full_path) {
        boolean flag = false;
        FileOutputStream fsave = null;
        try {
            File f = new File(full_path);
            if (!f.exists()) {
                f.createNewFile();
            }
            fsave = new FileOutputStream(f);
            fsave.write(arrbyte);
            flag = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fsave.flush();
                fsave.close();
            } catch (IOException ex) {
                LogUtils.debug("Error close FileOutputStream");
            }
            return flag;
        }
    }
    private static final int bufferSize = 8192;

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int read;
        while ((read = in.read(buffer, 0, bufferSize)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     *
     * @param bis
     * @return
     */
    public static byte[] writeBuffer2Byte(BufferedInputStream bis) {
        byte[] byteReturn = null;
        byte[] buffer = new byte[1024];
        long fileSize = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while (true) {
                int iBytes = bis.read(buffer);
                ////Tool.debug(iBytes);
                // If there was nothing read, get out of loop
                if (iBytes == -1) {
                    break;
                } else {
                    fileSize += iBytes;
                }
                baos.write(buffer, 0, iBytes);
            }
            byteReturn = baos.toByteArray();
        } catch (IOException ie) {
            ie.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
                bis.close();
            } catch (Exception e) {
            }
            return byteReturn;
        }
    }

    public static boolean writeFileText(String content, String path) {
        boolean flag = false;
        try {
            try ( Writer outw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"))) {
                outw2.write(content);
                flag = true;
            } catch (Exception e) {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static String readFileText(String path) {
        String Content = "";
        String sContent = "";
        try {
            FileInputStream fstream = new FileInputStream(path);
            try ( DataInputStream in = new DataInputStream(fstream)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((Content = br.readLine()) != null) {
                    sContent += Content + "\n";
                }
            }
        } catch (IOException e) {
            System.err.println("Tool : Error: ReadFile >> " + e.getMessage());
        }
        return sContent;
    }

    public static String getFieldName(Method method) {
        try {
            Class<?> clazz = method.getDeclaringClass();
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                if (method.equals(pd.getWriteMethod()) || method.equals(pd.getReadMethod())) {
                    return pd.getName();
                }
            }
        } catch (Exception e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return null;
    }

    public static boolean writeContent(BufferedInputStream bis, ByteArrayOutputStream baos) {
        boolean flag = true;
        byte[] buffer = new byte[1024];
        try {
            while (true) {
                int iBytes = bis.read(buffer);
                // If there was nothing read, get out of loop
                if (iBytes == -1) {
                    break;
                }
                baos.write(buffer, 0, iBytes);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
            flag = false;
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public static byte[] getBytesFromFile(File file) throws IOException, FileNotFoundException {
        InputStream fin = new FileInputStream(file);
        // Get the size of the file
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large" + file.getName());
            // File is too large
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = fin.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        // Close the input stream and return bytes
        fin.close();
        return bytes;
    }

//    public static BufferedImage resizeImage(BufferedImage originalImage, int type, int image_with) {
//        float IMG_HEIGHT = originalImage.getHeight() / ((float) originalImage.getWidth() / image_with);
//        BufferedImage resizedImage = new BufferedImage(image_with, (int) IMG_HEIGHT, type);
//        Graphics2D graphics2D = resizedImage.createGraphics();
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.drawImage(originalImage, 0, 0, image_with, (int) IMG_HEIGHT, null);
//        graphics2D.dispose();
//        return resizedImage;
//    }
//    public static void resizeAndWriteImage(InputStream ipst, int width, String realPath, String extention) {
//        try {
//            BufferedImage originalImage = null;
//            BufferedImage resizeImage = null;
//            originalImage = ImageIO.read(ipst);
//            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_RGB : originalImage.getType();
//            if (extention.startsWith(".")) {
//                extention = extention.substring(1);
//            }
//            //----------------
//            resizeImage = resizeImage(originalImage, type, width);
//            // Den buoc nay ma Write la ok
//            ImageIO.write(resizeImage, extention, new File(realPath));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void resize_MaxWith_write(InputStream ipst, int max_width, String realPath, String extention) {
//        try {
//            BufferedImage originalImage = null;
//            BufferedImage resizeImage = null;
//            originalImage = ImageIO.read(ipst);
//            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_RGB : originalImage.getType();
//            //----------------
//            if (originalImage.getWidth() > max_width) {
//                // resize va write
//                resizeImage = resizeImage(originalImage, type, max_width);
//
//            } else {
//                resizeImage = originalImage;
//            }
//            if (extention.startsWith(".")) {
//                extention = extention.substring(1);
//            }
//            ImageIO.write(resizeImage, extention, new File(realPath));
//            // Den buoc nay ma Write la ok
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public static InputStream resizeMaxWith(InputStream ipst, int max_width) {
        InputStream result = null;
        try {
            BufferedImage originalImage = ImageIO.read(ipst);
            //----------------
            if (originalImage.getWidth() > max_width) {
                BufferedImage newImg = Thumbnails.of(originalImage)
                        .width(max_width)
                        .outputQuality(1).asBufferedImage();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(newImg, "jpeg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
                result = new ByteArrayInputStream(os.toByteArray());
            } else {
                result = ipst;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void resizeMaxWithWriteImg(InputStream ipst, int max_width, String realPath, String extention) {
        try {
            BufferedImage originalImage = ImageIO.read(ipst);
            //----------------
            if (originalImage.getWidth() > max_width) {
                // resize va write
                Thumbnails.of(originalImage)
                        .width(max_width)
                        .outputFormat(extention)
                        .outputQuality(1)
                        .toFile(new File(realPath));
            } else {
                Thumbnails.of(originalImage)
                        .width(originalImage.getWidth())
                        .outputFormat(extention)
                        .outputQuality(1)
                        .toFile(new File(realPath));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resizeMaxWithWriteImg(URL url, int max_width, String realPath, String extention) {
        try {
            BufferedImage originalImage = ImageIO.read(url.openStream());
            //----------------
            if (originalImage.getWidth() > max_width) {
                // resize va write
                Thumbnails.of(url)
                        .width(max_width)
                        .outputQuality(1)
                        // .outputFormat(extention)
                        .toFile(new File(realPath));
            } else {
                Thumbnails.of(url)
                        .width(originalImage.getWidth())
                        .outputFormat(extention)
                        .outputQuality(1)
                        .toFile(new File(realPath));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean writeImg(InputStream ipst, String realPath, String extention) {
        try {
            BufferedImage originalImage = ImageIO.read(ipst);
            Thumbnails.of(originalImage)
                    .width(originalImage.getWidth())
                    .outputFormat(extention)
                    .outputQuality(1)
                    .toFile(new File(realPath));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void writeImg(URL url, String realPath, String extention) {
        try {
            BufferedImage originalImage = ImageIO.read(url.openStream());
            Thumbnails.of(url)
                    .width(originalImage.getWidth())
                    .outputQuality(1)
                    .toFile(new File(realPath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resizeWriteImg(InputStream ips, int width, String realPath, String extention) {
        try {
            // resize va write
            Thumbnails.of(ips)
                    .width(width)
                    .outputFormat(extention)
                    .outputQuality(1)
                    .toFile(new File(realPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resizeWriteImg(URL url, int width, String realPath, String extention) {
        try {
            //----------------
            // resize va write
            Thumbnails.of(url)
                    .width(width)
                    // .outputFormat(extention)
                    .outputQuality(1)
                    .toFile(new File(realPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// Copies src file to dst file.
    // If the dst file does not exist, it is created

    public static void copy(String src, String dst) throws IOException {
        File f_src = new File(src);
        File f_dst = new File(dst);
        InputStream in = new FileInputStream(f_src);
        OutputStream out = new FileOutputStream(f_dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    //

    public static void createTempFile(String pattern, String suffix) {//yyyymmdd,.gif
        try {
            // Create temp file.
            File temp = File.createTempFile(pattern, suffix);

            // Delete temp file when program exits.
            temp.deleteOnExit();

            // Write to temp file
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            out.write("aString");
            out.close();
        } catch (IOException e) {
        }
    }
    //

    public static void moveFile(String src, String des) {
        // File (or directory) to be moved
        File file = new File(src);

        // Destination directory
        File dir = new File(des);

        // Move file to new directory
        boolean success = file.renameTo(new File(dir, file.getName()));
        if (!success) {
            // File was not successfully moved
        }

    }

    /**
     * Renaming a File or Directory
     *
     * @param oldName
     * @param newName
     */
    public static void renameFileOrDir(String oldName, String newName) {
        // Renaming a File or Directory
        // File (or directory) with old name
        File oldFile = new File(oldName);
        // File (or directory) with new name
        File newFile = new File(newName);
        // Rename file (or directory)
        boolean success = oldFile.renameTo(newFile);
        if (!success) {
            LogUtils.debug("Rename file not successfully");
        }
    }

    public static boolean createNewFile(String filename) {
        try {
            File file = new File(filename);

            // Create file if it does not exist
            boolean success = file.createNewFile();
            if (success) {
                // File did not exist and was created
                return true;
            } else {
                // File already exists
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    //
    public static long getFileSize(String filename) {
        File file = new File(filename);
        // Get the number of bytes in the file
        long length = file.length();
        return length;

    }

    //
    public static void listFiles(String pathDir) {
        File dir = new File(pathDir);

        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
            }
        }

        // It is also possible to filter the list of returned files.
        // This example does not return any files that start with `.'.
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return !name.startsWith(".");
            }
        };
        children = dir.list(filter);

        // The list of files can also be retrieved as File objects
        File[] files = dir.listFiles();

        // This filter only returns directories
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        files = dir.listFiles(fileFilter);

    }

    //
    public static boolean createDir(String dir) {
        // Create a directory; all ancestor directories must exist
        boolean success = true;
//  boolean success = (new File(dir)).mkdir();
//  if (!success) {
//      // Directory creation failed
//  }

        // Create a directory; all non-existent ancestor directories are
        // automatically created
        success = (new File(dir)).mkdirs();
        if (!success) {
            // Directory creation failed
            return false;

        }
        return success;
    }

    //
    public static void delete(String fileName) {
        try {
            // Construct a File object for the file to be deleted.
            File target = new File(fileName);

            if (!target.exists()) {
                System.err.println("File " + fileName
                        + " not present to begin with!");
                return;
            }

            // Quick, now, delete it immediately:
            if (target.delete()) {
                System.err.println("** Deleted " + fileName + " **");
            } else {
                System.err.println("Failed to delete " + fileName);
            }
        } catch (SecurityException e) {
            System.err.println("Unable to delete " + fileName + "("
                    + e.getMessage() + ")");
        }
    }

    //
    public static String getExtension(String fileName) {
        String ext = "";
        try {
            if (StringUtils.isEmpty(fileName)) {
                return ext;
            }
            int index = fileName.lastIndexOf(".");
            if (index != -1) {
                ext = fileName.substring(index + 1);
            } else {
                return ext;
            }
        } catch (Exception e) {
        }
        return ext;
    }

    public static String getFileName(String fullName) {
        String name = "";
        try {
            if (StringUtils.isEmpty(fullName)) {
                return name;
            }
            int index = fullName.lastIndexOf(".");
            if (index != -1) {
                name = fullName.substring(0, index);
            } else {
                return name;
            }
        } catch (Exception e) {
        }
        return name;
    }

    //
    public static String converTxtFile2String(BufferedReader br) {
        if (br == null) {
            return null;
        }
        String result = "";
        try {
            String line = "";
            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception ex) {
            LogUtils.debug("convertTxtFie2String:" + ex.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

    public static boolean validExtention(String[] extention, String ext) {
        boolean flag = false;
        try {
            for (String one : extention) {
                if (ext.equals(one.trim())) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return flag;
    }

}
