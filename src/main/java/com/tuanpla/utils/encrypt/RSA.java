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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tuanpla
 */
public class RSA {

    static Logger logger = LogManager.getLogger(RSA.class);
    public static final int KEY_SIZE = 2048;
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
            try ( ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile))) {
                publicKeyOS.writeObject(key.getPublic());
            }
            try ( ObjectOutputStream privateKeyOS = new ObjectOutputStream(
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
