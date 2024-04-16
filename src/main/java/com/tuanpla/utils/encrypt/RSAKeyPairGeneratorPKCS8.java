/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tuanpla.utils.encrypt;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author tuanp
 */
public class RSAKeyPairGeneratorPKCS8 {

    public static void main(String[] args) throws Exception {
        // Khởi tạo generator cho cặp khóa RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Độ dài khóa là 2048 bit

        // Tạo cặp khóa RSA
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Lưu trữ khóa công khai dưới dạng PKCS#8
        try (FileOutputStream fos = new FileOutputStream("public_key.der")) {
            fos.write(publicKey.getEncoded());
        }

        // Lưu trữ khóa riêng tư dưới dạng PKCS#8
        try (FileOutputStream fos = new FileOutputStream("private_key.der")) {
            fos.write(privateKey.getEncoded());
        }
    }
}
