package com.cmlx.commons.encrypt;

import com.cmlx.commons.support.StreamUtility;
import com.cmlx.commons.support.StringUtility;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:13
 * @Desc ->
 **/
@UtilityClass
public class EncryptorRsaUtility {
    /**
     * RSA最大加密明文大小
     */
    private final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private final int MAX_DECRYPT_BLOCK = 128;


    public String sign(String text, String privateKey, OPERATION_RSA operationRsa) {
        return sign(text, privateKey, operationRsa, Consts.UTF_8.displayName());
    }

    public String sign(String text, String privateKey, OPERATION_RSA operationRsa, String charset) {
        if (!StringUtility.allNotBlank(text, privateKey)) return text;
        try {
            Signature signature = createSignature(operationRsa);
            PrivateKey key = getPrivateKeyFromPKCS8(operationRsa.getAlgorithm(), privateKey);
            signature.initSign(key);
            if (StringUtility.hasLength(charset)) {
                signature.update(text.getBytes(charset));
            } else {
                signature.update(text.getBytes());
            }
            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {
            return text;
        }
    }

    public String encrypt(String text, String publicKey, String charset) {
        try {
            PublicKey key = getPublicKeyFromX509(OPERATION_RSA.RSA, publicKey);
            Cipher cipher = Cipher.getInstance(OPERATION_RSA.RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] data = StringUtility.hasLength(charset) ? text.getBytes(charset) : text.getBytes();
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
            out.close();
            return StringUtility.hasLength(charset) ? new String(encryptedData, charset)
                    : new String(encryptedData);
        } catch (Exception e) {
            return text;
        }
    }

    public String decrypt(String text, String privateKey, String charset) {
        try {
            PrivateKey key = getPrivateKeyFromPKCS8(text, privateKey);
            Cipher cipher = Cipher.getInstance(OPERATION_RSA.RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = StringUtility.hasLength(charset)
                    ? Base64.decodeBase64(text.getBytes(charset))
                    : Base64.decodeBase64(text.getBytes());
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return StringUtility.hasLength(charset) ? new String(decryptedData, charset)
                    : new String(decryptedData);
        } catch (Exception e) {
            return text;
        }
    }

    public Signature createSignature(OPERATION_RSA operationRsa) throws NoSuchAlgorithmException {
        return Signature.getInstance(operationRsa.getOperation());
    }

    public PrivateKey getPrivateKeyFromPKCS8(String algorithm, String privateKey) throws Exception {
        if (!StringUtility.hasLength(algorithm) || !StringUtility.hasLength(privateKey)) return null;
        return getPrivateKeyFromPKCS8(algorithm, new ByteArrayInputStream(privateKey.getBytes()));
    }

    public PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = StreamUtility.readText(ins).getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, String publicKey) throws Exception {
        if (!StringUtility.hasLength(algorithm) || !StringUtility.hasLength(publicKey)) return null;
        return getPublicKeyFromX509(algorithm, new ByteArrayInputStream(publicKey.getBytes()));
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        StreamUtility.io(new InputStreamReader(ins), writer);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    public enum OPERATION_RSA {
        MD2withRSA(OPERATION_RSA.RSA, "MD2withRSA"),
        MD5withRSA(OPERATION_RSA.RSA, "MD5withRSA"),
        SHA1withRSA(OPERATION_RSA.RSA, "SHA1withRSA"),
        SHA256withRSA(OPERATION_RSA.RSA, "SHA256withRSA");
        public static final String RSA = "RSA";
        private String algorithm;
        private String operation;

        OPERATION_RSA(String algorithm, String operation) {
            this.algorithm = algorithm;
            this.operation = operation;
        }

        public String getOperation() {
            return operation;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }
}

