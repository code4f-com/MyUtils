/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuanpla.utils.encrypt;

/**
 *
 * @author TUANPLA
 */
import com.tuanpla.utils.file.FileUtils;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

//import com.fathomdb.Utf8;
//import com.fathomdb.utils.Base64;
public class KeyParser {

    public KeyParser() {
    }

    static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";

    // private KeyPair tryParsePrivateKey(String data) {
    // PEMReader pemReader = new PEMReader(new StringReader(data), new PasswordFinder() {
    //
    // @Override
    // public char[] getPassword() {
    // return "notasecret".toCharArray();
    // }
    // });
    // try {
    // return (KeyPair) pemReader.readObject();
    // } catch (IOException e) {
    // log.debug("Unable to parse pem data", e);
    // return null;
    // } finally {
    // Io.safeClose(pemReader);
    // }
    // }
    public static Object parse(byte[] data) {
        Object key = null;

        if (key == null) {
            String s = tryDecodeAsString(data);
            if (s != null) {
                key = parse(s);
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String pubStr = FileUtils.readFileText("D:\\WORK\\_Project_GW\\6x88\\ScanXsAHP\\config\\Public.pem");
        System.out.println(pubStr);
        Object result = parse(pubStr);
        if (result instanceof PublicKey) {
            PublicKey key = (PublicKey) result;
            System.out.println("Success");
        }

    }

    public static Object parse(String s) {
        Object key = null;

        if (key == null) {
            if (s.contains(BEGIN_PRIVATE_KEY)) {
                String payload = s.substring(s.indexOf(BEGIN_PRIVATE_KEY) + BEGIN_PRIVATE_KEY.length());
                if (payload.contains(END_PRIVATE_KEY)) {
                    payload = payload.substring(0, payload.indexOf(END_PRIVATE_KEY));

                    key = tryParsePemFormat(payload);
                }
            }
        }

        if (key == null) {
            try {
                PemReader reader = new PemReader(new StringReader(s));
                PemObject pemObject = reader.readPemObject();
                reader.close();

                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PrivateKey privateKey = kf.generatePrivate(keySpec);
                if (privateKey instanceof RSAPrivateCrtKey) {
                    RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) privateKey;
                    RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(
                            rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent());
                    PublicKey publicKey = kf.generatePublic(publicKeySpec);
                    key = new KeyPair(publicKey, privateKey);
                } else {
                    key = privateKey;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (key == null) {
            try {
                // TODO: Check if looks like base64??
                byte[] fromBase64 = Base64.decode(s);

                key = parse(fromBase64);
            } catch (Exception e) {
                e.printStackTrace();
//                log.debug("Cannot decode as base64", e);
            }
        }

        return key;
    }

    private static String tryDecodeAsString(byte[] data) {
        try {
            CharsetDecoder decoder = Charset.forName("UTF8").newDecoder();
            decoder.onMalformedInput(CodingErrorAction.REPORT);
            decoder.onUnmappableCharacter(CodingErrorAction.REPORT);

            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            CharBuffer charBuffer = decoder.decode(byteBuffer);
            return charBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
//            log.debug("Cannot decode as string", e);
            return null;
        }
    }

    private static PrivateKey tryParsePemFormat(String data) {
        try {
            byte[] encoded = Base64.decode(data);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privKey = kf.generatePrivate(keySpec);

            return privKey;
        } catch (Exception e) {
            e.printStackTrace();
//            log.debug("Error parsing pem data", e);
            return null;
        }
    }
}
