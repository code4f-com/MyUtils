/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tuanpla.utils.encrypt;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author tuanpla
 */
public class AESUtil {

    private static final String AES = "AES";

    public static void main(String[] args) throws Exception {
        // Khởi tạo và tạo khóa AES
        SecretKey secretKey = generateAESKey();

        // Chuỗi cần mã hóa
        String originalString = "Hello, world!";

        // Mã hóa chuỗi
        String encryptedString = encrypt(originalString, secretKey);
        System.out.println("Encrypted String: " + encryptedString);

        // Giải mã chuỗi
        String decryptedString = decrypt(encryptedString, secretKey);
        System.out.println("Decrypted String: " + decryptedString);
    }

    // Tạo khóa AES
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, new SecureRandom()); // Độ dài khóa: 256 bit
        return keyGenerator.generateKey();
    }

    public static SecretKey generateAESKey(int size, String algorithm) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(size, SecureRandom.getInstance(algorithm)); // Độ dài khóa: 256 bit
        return keyGenerator.generateKey();
    }

    // Mã hóa chuỗi sử dụng AES
    public static String encrypt(String strToEncrypt, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Giải mã chuỗi sử dụng AES
    public static String decrypt(String strToDecrypt, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
        return new String(decryptedBytes);
    }
}
