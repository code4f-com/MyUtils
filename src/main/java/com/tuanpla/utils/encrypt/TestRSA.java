/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tuanpla.utils.encrypt;

import java.security.KeyPair;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author tuanp
 */
public class TestRSA {

    static String pathStore = "/Users/tuanpla/Project/ComWork/rsa/";
    private static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;

    public static void main(String[] args) {
        try {
//            Fix Error java.security.NoSuchProviderException No such provider: BC
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            KeyPair keyPair = RSA.generateRSAKeyPair(2048);
            RSA.writeKeyPKCS(keyPair.getPrivate(), pathStore + "private.psk1.pem");
            RSA.writeKeyPKCS(keyPair.getPublic(), pathStore + "public.psk1.pem");
            //
            RSA.writePemFile(keyPair.getPublic(), "Mô tả", pathStore + "public.pem");
            RSA.writePemFile(keyPair.getPublic(), "Mô tả", pathStore + "private.pem");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
