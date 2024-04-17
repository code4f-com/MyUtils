/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.file;

import com.tuanpla.utils.common.LogUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class DataConvert {

    private static Logger logger = LoggerFactory.getLogger(DataConvert.class);

    public static byte[] InputStream2Bytes(InputStream is) {
        if (is == null) {
            return null;
        }
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        byte[] dataReturn = null;
        try {
            bis = new BufferedInputStream(is);
            baos = new ByteArrayOutputStream();

            while (true) {
                int iBytes = bis.read(buffer);
                if (iBytes == -1) {
                    break;
                }
                baos.write(buffer, 0, iBytes);
            }
            dataReturn = baos.toByteArray();
        } catch (IOException ex) {
            logger.error(LogUtils.getLogMessage(ex));
        } finally {
            try (is) {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                logger.error(LogUtils.getLogMessage(e));
            }
        }
        return dataReturn;
    }

    public static byte[] InputStream2Bytes(ImageInputStream is) {
        if (is == null) {
            return null;
        }
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        byte[] dataReturn = null;
        try {
            bis = new BufferedInputStream((InputStream) is);
            baos = new ByteArrayOutputStream();

            while (true) {
                int iBytes = bis.read(buffer);
                if (iBytes == -1) {
                    break;
                }
                baos.write(buffer, 0, iBytes);
            }
            dataReturn = baos.toByteArray();
        } catch (IOException ex) {
            logger.error(LogUtils.getLogMessage(ex));
        } finally {
            try (is) {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception e) {
                logger.error(LogUtils.getLogMessage(e));
            }
        }
        return dataReturn;
    }

    public static String InputStream2String(InputStream is) {
        if (is == null) {
            return null;
        }
        String strReturn = null;
        byte[] buffer = new byte[1024];

        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new BufferedInputStream(is);
            baos = new ByteArrayOutputStream();

            int iBytes = bis.read(buffer);
            while (iBytes > 0) {
                baos.write(buffer, 0, iBytes);
                iBytes = bis.read(buffer);
            }
            strReturn = baos.toString();
        } catch (IOException ex) {
            logger.error(LogUtils.getLogMessage(ex));
        } finally {
            try {
                is.close();
                if (bis != null) {
                    bis.close();
                }
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                logger.error(LogUtils.getLogMessage(e));
            }

        }
        return strReturn;
    }

    public static int InputStream2File(InputStream is, String sPath) {
        if (is == null) {
            return -1;
        }
        try (FileOutputStream fout = new FileOutputStream(sPath)) {
            byte[] b = InputStream2Bytes(is);
            fout.write(b);
            fout.flush();
            return b.length;
        } catch (IOException ex) {
            logger.error(LogUtils.getLogMessage(ex));
            return -1;
        }
    }

    public static int Bytes2File(byte[] bInput, String sPath) {
        if (bInput == null) {
            return -1;
        }
        try (FileOutputStream fout = new FileOutputStream(sPath)) {
            fout.write(bInput);
            fout.flush();
        } catch (IOException ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return bInput.length;
    }

    //////////////////////////////////////////////////////////////////////
    public static byte[] PictureMsgEncode(String sText, byte[] bOtb) {
        try {
            ByteArrayOutputStream encoded = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(encoded);
            dout.writeByte(0x30); //version 0
            dout.writeByte(0x00); //"00"
            if (sText != null) {
                dout.writeShort(sText.length());
                dout.writeBytes(sText);
            } else {
                dout.writeShort(0x0000);
            }
            dout.writeByte(0x02);
            dout.writeShort(0x0100);
            encoded.write(bOtb);
            return encoded.toByteArray();
        } catch (IOException ex) {
            logger.error(LogUtils.getLogMessage(ex));
            return null;
        }
    }

    public static byte[] getBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = null;
        try (InputStream is = new FileInputStream(file)) {
            logger.debug("FileInputStream is " + file);
            // Get the size of the file
            long length = file.length();
            logger.debug("ength of " + file + " is " + length + "\n");
            /*
            * You cannot create an array using a long type. It needs to be an int
            * type. Before converting to an int type, check to ensure that file is
            * not loarger than Integer.MAX_VALUE;
             */
            if (length > Integer.MAX_VALUE) {
                logger.debug("File is too large to process");
                return null;
            }   // Create the byte array to hold the data
            bytes = new byte[(int) length];
            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while ((offset < bytes.length)
                    && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
                offset += numRead;

            }   // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
        } catch (Exception ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return bytes;

    }

    public static String byte2Base64(byte[] data) {
        try {
            return Base64.encodeBase64String(data);
        } catch (Exception e) {
            return "";
        }
    }
}
