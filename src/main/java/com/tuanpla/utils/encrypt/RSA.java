/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.encrypt;

import com.tuanpla.utils.logging.LogUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class RSA {

    static Logger logger = LoggerFactory.getLogger(RSA.class);
    /**
     * String to hold name of the encryption algorithm.
     */
    private static final String ALGORITHM = "RSA";
    /**
     * String to hold the name of the private key file.
     */
    public static final String PRIVATE_KEY_FILE = "/config/rsa.key/private.key";
    /**
     * String to hold name of the public key file.
     */
    public static final String PUBLIC_KEY_FILE = "/config/rsa.key/public.key";
    // Key ------------
    public static PublicKey RSA_PUBLIC_KEY;
    public static PrivateKey RSA_PRIVATE_KEY;

    public static void createRSA() {
        try {
            // Check if the pair of keys are present else generate those.
            if (!areKeysPresent()) {
                // Method generates a pair of keys using the RSA algorithm and stores it
                // in their respective files
                generateKey();
            }
            // Read RSA Key From File
            ObjectInputStream inputStream = null;

            // Encrypt the string using the public key
            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            RSA_PUBLIC_KEY = (PublicKey) inputStream.readObject();
            // Decrypt the cipher text using the private key.
            inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
            RSA_PRIVATE_KEY = (PrivateKey) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
    }

    private static void generateKey() throws IOException {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            // Create files to store public and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();
            try (ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile))) {
                publicKeyOS.writeObject(key.getPublic());
            }
            try (ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                    new FileOutputStream(privateKeyFile))) {
                privateKeyOS.writeObject(key.getPrivate());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The method checks if the pair of public and private key has been
     * generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent() {

        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }
    /// *********** OLD CODE

    public static String encript(PublicKey publicKey, String input) {
        String str = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] x = cipher.doFinal(input.getBytes());
            str = toHexadecimal(x);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return str;
    }

    public static String deEncript(PrivateKey privateKey, String input) {
        String str = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            byte[] temByte = hexStringToByteArray(input);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] y = cipher.doFinal(temByte);
            str = new String(y);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return str;
    }

//    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(1024);
//        KeyPair kp = keyGen.genKeyPair();
//
//
//        PublicKey publicKey = kp.getPublic();
//        PrivateKey privateKey = kp.getPrivate();
//        Tool.debug("PublicKey-getAlgorithm:" + publicKey.getAlgorithm());
//        Tool.debug("PublicKey-getFormat:" + publicKey.getFormat());
//        Tool.debug("PublicKey-getFormat:" + publicKey.toString());
//        String text = "Tuan PLA";
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] x = cipher.doFinal(text.getBytes());
//
//        String tem = toHexadecimal(x);
//        byte[] temByte = hexStringToByteArray(tem);
//
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] y = cipher.doFinal(temByte);
//
//        Tool.debug(new String(y));
//    }
    private static String toHex(byte[] buf) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    private static String toHexadecimal(byte[] data) {
        String result = "";
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        String cadAux;
        boolean ult0 = false;
        int leido = input.read();
        while (leido != -1) {
            cadAux = Integer.toHexString(leido);
            if (cadAux.length() < 2) {
                result += "0";
                if (cadAux.length() == 0) {
                    ult0 = true;
                }
            } else {
                ult0 = false;
            }
            result += cadAux;
            leido = input.read();
        }
        if (ult0) {
            result = result.substring(0, result.length() - 2) + result.charAt(result.length() - 1);
        }
        return result;
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
