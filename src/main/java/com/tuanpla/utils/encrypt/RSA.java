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
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class RSA {

    private static final Logger logger = LoggerFactory.getLogger(RSA.class);
    private static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;
    public static final int KEY_SIZE_2048 = 2048;
    public static final int KEY_SIZE_1024 = 1024;
    /**
     * String to hold name of the encryption algorithm.
     */
    private static final String ALGORITHM = "RSA";

    public static PublicKey readPublicObject(String pathPublic) {
        PublicKey prublicKey = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(pathPublic));
            prublicKey = (PublicKey) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return prublicKey;
    }

    public static PrivateKey readPrivateObject(String pathPrivate) {
        PrivateKey privateKey = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(pathPrivate));
            privateKey = (PrivateKey) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return privateKey;
    }

    /**
     * For 2048 byte
     *
     * @param keySize
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static KeyPair generateRSAKeyPair(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (keySize % 1024 != 0) {
            // TODO KeySize invalid
        }
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", PROVIDER);
        generator.initialize(keySize);
        KeyPair keyPair = generator.generateKeyPair();
        logger.info("RSA key pair generated.");
        return keyPair;
    }

    public static void main(String[] args) {

    }

    public static void generateKey(String pathPrivate, String pathPublic) throws IOException {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();
            logger.info(String.format("generateKey successfully KeyPairGenerator"));
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
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error SHA26: ", e);
            return null;
        }
    }

    /**
     * Encrypt with PublicKey
     *
     * @param publicKey
     * @param input
     * @return
     */
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
//        return new String(Base64.encodeBase64(sign.sign()), StandardCharsets.UTF_8); // using org.apache.commons.codec.binary.Base64
        return new String(Base64.getEncoder().encode(sign.sign()), StandardCharsets.UTF_8);
    }

    public static boolean verify(PublicKey publicKey, String message, String signature) throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA1withRSA");
        sign.initVerify(publicKey);
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return sign.verify(Base64.getDecoder().decode(signature.getBytes(StandardCharsets.UTF_8)));
    }

    public static String encrypt(String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes(StandardCharsets.UTF_8)));
        return Base64.getEncoder().encodeToString(cipher.doFinal(rawText.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), StandardCharsets.UTF_8);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), StandardCharsets.UTF_8);
    }

    ///---------------
    public static byte[] encrypt(String data, byte[] publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    // -- Thiếu decrypt privateKey byte
    //------
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

    //--- Ghi file theo cac chuan ---//
    /**
     * PKCS#1 store file public_key.pem/private_key.pem ==> ASN.1 (Abstract
     * Syntax Notation One) PKCS#8 store file public_key.der/private_key.der ==>
     * Private-Key Information Syntax Standard
     *
     * @param publicKey
     * @param desStoreFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeKeyPKCS(PublicKey publicKey, String desStoreFile) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(desStoreFile)) {
            fos.write(publicKey.getEncoded());
        }
    }

    public static void writeKeyPKCS(PrivateKey privateKey, String desStoreFile) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(desStoreFile)) {
            fos.write(privateKey.getEncoded());
        }
    }

    public static void writeKeyPKCS(Key keyStore, String desStoreFile) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(desStoreFile)) {
            fos.write(keyStore.getEncoded());
        }
    }

    /**
     * Use for Write PrivateKey/PublicKey to file
     *
     * @param key là một đối tượng PublicKey hoặc PrivateKey
     * @param description
     * @param desStoreFile Destination store file .pem
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writePemFile(Key key, String description, String desStoreFile) throws FileNotFoundException, IOException {
        PemFile pemFile = new PemFile(key, description);
        pemFile.write(desStoreFile);
        logger.info(String.format("%s successfully writen in file %s.", description, desStoreFile));
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
