/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.encrypt;

import com.tuanpla.utils.common.HexUtil;
import com.tuanpla.utils.logging.LogUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class RSA {

    private static Logger logger = LoggerFactory.getLogger(RSA.class);

//    public static final int KEY_SIZE = 2048;
    public static final int KEY_SIZE = 1024;
    /**
     * String to hold name of the encryption algorithm.
     */
    private static final String ALGORITHM = "RSA";
    /**
     * String to hold the name of the private key file.
     */
//    public static final String PRIVATE_KEY_FILE = "/config/rsa.key/private.key";
    /**
     * String to hold name of the public key file.
     */
//    public static final String PUBLIC_KEY_FILE = "/config/rsa.key/public.key";
    // Key ------------
    public static PublicKey RSA_PUBLIC_KEY;
    public static PrivateKey RSA_PRIVATE_KEY;

    public static void createRSA(String pathPrivate, String pathPublic) {
        try {
            // Check if the pair of keys are present else generate those.
            if (!areKeysPresent(pathPrivate, pathPublic)) {
                // Method generates a pair of keys using the RSA algorithm and stores it
                // in their respective files
                generateKey(pathPrivate, pathPublic);
            }
            // Read RSA Key From File
            ObjectInputStream inputStream = null;
            // Encrypt the string using the public key
            inputStream = new ObjectInputStream(new FileInputStream(pathPublic));
            RSA_PUBLIC_KEY = (PublicKey) inputStream.readObject();
            // Decrypt the cipher text using the private key.
            inputStream = new ObjectInputStream(new FileInputStream(pathPrivate));
            RSA_PRIVATE_KEY = (PrivateKey) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
    }

    /**
     * For 2048 byte
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(KEY_SIZE);
        KeyPair keyPair = generator.generateKeyPair();
        logger.info("RSA key pair generated.");
        return keyPair;
    }

    public static void writePemFile(Key key, String description, String filename)
            throws FileNotFoundException, IOException {
        PemFile pemFile = new PemFile(key, description);
        pemFile.write(filename);
        logger.info(String.format("%s successfully writen in file %s.", description, filename));
    }

//    private static void generateKey() throws IOException {
//        try {
//            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
//            keyGen.initialize(1024);
//            final KeyPair key = keyGen.generateKeyPair();
//
//            File privateKeyFile = new File(PRIVATE_KEY_FILE);
//            File publicKeyFile = new File(PUBLIC_KEY_FILE);
//
//            // Create files to store public and private key
//            if (privateKeyFile.getParentFile() != null) {
//                privateKeyFile.getParentFile().mkdirs();
//            }
//            privateKeyFile.createNewFile();
//
//            if (publicKeyFile.getParentFile() != null) {
//                publicKeyFile.getParentFile().mkdirs();
//            }
//            publicKeyFile.createNewFile();
//            try (ObjectOutputStream publicKeyOS = new ObjectOutputStream(
//                    new FileOutputStream(publicKeyFile))) {
//                publicKeyOS.writeObject(key.getPublic());
//            }
//            try (ObjectOutputStream privateKeyOS = new ObjectOutputStream(
//                    new FileOutputStream(privateKeyFile))) {
//                privateKeyOS.writeObject(key.getPrivate());
//            }
//        } catch (NoSuchAlgorithmException | IOException e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
    
    public static void generateKey(String pathPrivate, String pathPublic) throws IOException {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();
            //-------------
            File privateKeyFile = new File(pathPrivate);
            File publicKeyFile = new File(pathPublic);

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
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * The method checks if the pair of public and private key has been
     * generated.
     *
     * @param pathPrivate
     * @param pathPublic
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent(String pathPrivate, String pathPublic) {
        File privateKey = new File(pathPrivate);
        File publicKey = new File(pathPublic);
        return privateKey.exists() && publicKey.exists();
    }

/// *********** OLD CODE
    public static String SHA256(String inp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(inp.getBytes(StandardCharsets.UTF_8)); // Change this to "UTF-16" if
            // needed
            byte[] digest = md.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (Exception e) {
            logger.error("Error SHA26: ", e);
            return null;
        }
    }

    public static String encript(PublicKey publicKey, String input) {
        String str = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] x = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            str = HexUtil.byteToHex(x);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return str;
    }

    public static String deEncript(PrivateKey privateKey, String hexStr) {
        String str = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            byte[] temByte = HexUtil.hexToBytes(hexStr);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] y = cipher.doFinal(temByte);
            str = new String(y);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return str;
    }

    public static PrivateKey generatePrivateKey(KeyFactory factory, String filename) throws InvalidKeySpecException, FileNotFoundException, IOException {
        PemFile pemFile = new PemFile(filename);
        byte[] content = pemFile.getPemObject().getContent();
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
        return factory.generatePrivate(privKeySpec);
    }

    public static PublicKey generatePublicKey(KeyFactory factory, String filename) throws InvalidKeySpecException, FileNotFoundException, IOException {
        PemFile pemFile = new PemFile(filename);
        byte[] content = pemFile.getPemObject().getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        return factory.generatePublic(pubKeySpec);
    }

    public static String sign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sign = Signature.getInstance("SHA1withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.encodeBase64(sign.sign()), StandardCharsets.UTF_8);
    }

    public static boolean verify(PublicKey publicKey, String message, String signature) throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA1withRSA");
        sign.initVerify(publicKey);
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return sign.verify(Base64.decodeBase64(signature.getBytes(StandardCharsets.UTF_8)));
    }

    public static String encrypt(String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), StandardCharsets.UTF_8);
    }

    public static byte[] encrypt(String data, byte[] publicKey) throws BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, byte[] base64PrivateKey) throws IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(java.util.Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    public static PublicKey getPublicKey(byte[] publicKeyByte) {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(byte[] privateKeyByte) throws NoSuchAlgorithmException {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
        KeyFactory keyFactory = null;
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }
//    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(1024);
//        KeyPair kp = keyGen.genKeyPair();
//
//        PublicKey publicKey = kp.getPublic();
//        PrivateKey privateKey = kp.getPrivate();
//        LogUtils.debug("PublicKey-getAlgorithm:" + publicKey.getAlgorithm());
//        LogUtils.debug("PublicKey-getFormat:" + publicKey.getFormat());
//        LogUtils.debug("PublicKey-getFormat:" + publicKey.toString());
//        String text = "Tuan PLA";
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] x = cipher.doFinal(text.getBytes());
//
//        String tempHex = HexUtil.byteToHex(x);
//        byte[] tempByte = HexUtil.hexToBytes(tempHex);
//
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] y = cipher.doFinal(tempByte);
//
//        LogUtils.debug(new String(y));

//        Security.addProvider(new BouncyCastleProvider());
//        logger.info("BouncyCastle provider added.");
//
//        KeyPair keyPair = generateRSAKeyPair();
//        RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
//        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
//
//        writePemFile(priv, "RSA PRIVATE KEY", "Private.pem");
//        writePemFile(pub, "RSA PUBLIC KEY", "Public.pem");
//    }
}
