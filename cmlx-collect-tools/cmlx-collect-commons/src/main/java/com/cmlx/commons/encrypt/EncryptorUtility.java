package com.cmlx.commons.encrypt;

import com.cmlx.commons.exception.EXPF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:12
 * @Desc -> 加密解密工具类
 **/
@Slf4j
public class EncryptorUtility {

    private String key;

    public EncryptorUtility(String key) {
        this.key = key;
    }

    /**
     * 获取消息摘要。base64获取null
     *
     * @param operation
     * @return
     */
    private MessageDigest getDigest(OPERATION operation) {
        switch (operation) {
            case MD2:
                return DigestUtils.getMd2Digest();
            case MD5:
                return DigestUtils.getMd5Digest();
            case SHA1:
                return DigestUtils.getSha1Digest();
            case SHA256:
                return DigestUtils.getSha256Digest();
            case SHA384:
                return DigestUtils.getSha384Digest();
            case SHA512:
                return DigestUtils.getSha512Digest();
            default:
                return null;
        }
    }

    /**
     * 使用key进行加密
     *
     * @param text
     * @param operation
     * @return
     */
    public String encrypt(String text, OPERATION operation) {
        return this.encryptWithSalt(text, key, operation);
    }

    /**
     * 使用key进行加密
     *
     * @param input
     * @param operation
     * @return
     * @throws IOException
     */
    public String encrypt(InputStream input, OPERATION operation) throws IOException {
        return this.encryptWithSalt(input, key, operation);
    }

    /**
     * 普通加密
     *
     * @param text
     * @param operation
     * @return
     */
    public String encryptNoSalt(String text, OPERATION operation) {
        if (org.springframework.util.StringUtils.isEmpty(text)) {
            return null;
        }
        switch (operation) {
            case MD2:
                return DigestUtils.md2Hex(text);
            case MD5:
                return DigestUtils.md5Hex(text);
            case SHA1:
                return DigestUtils.sha1Hex(text);
            case SHA256:
                return DigestUtils.sha256Hex(text);
            case SHA384:
                return DigestUtils.sha384Hex(text);
            case SHA512:
                return DigestUtils.sha512Hex(text);
            case BASE64:
                Base64 base64 = new Base64();
                return base64.encodeAsString(StringUtils.getBytesUtf8(text));
            default:
                return null;
        }
    }

    /**
     * 普通加密
     *
     * @param input
     * @param operation
     * @return
     * @throws IOException
     */
    public String encryptNoSalt(InputStream input, OPERATION operation) throws IOException {
        if (null == input) {
            return null;
        }
        switch (operation) {
            case MD2:
                return DigestUtils.md2Hex(input);
            case MD5:
                return DigestUtils.md5Hex(input);
            case SHA1:
                return DigestUtils.sha1Hex(input);
            case SHA256:
                return DigestUtils.sha256Hex(input);
            case SHA384:
                return DigestUtils.sha384Hex(input);
            case SHA512:
                return DigestUtils.sha512Hex(input);
            case BASE64:
                byte[] data = new byte[input.available()];
                input.read(data);
                Base64 base64 = new Base64();
                return base64.encodeAsString(data);
            default:
                return null;
        }
    }

    public String encryptWithSalt(String text, String salt, OPERATION operation) {
        if (org.springframework.util.StringUtils.isEmpty(text) || org.springframework.util.StringUtils.isEmpty(salt)) {
            return null;
        }
        MessageDigest digest = this.getDigest(operation);
        if (null != digest) {
            DigestUtils.updateDigest(digest, salt);
            return Hex.encodeHexString(digest.digest(StringUtils.getBytesUtf8(text)));
        } else {
            Base64 base64 = new Base64();
            return base64.encodeAsString(StringUtils.getBytesUtf8(text));
        }
    }

    public String encryptWithSalt(InputStream input, String salt, OPERATION operation) throws IOException {
        if (null == input || org.springframework.util.StringUtils.isEmpty(salt)) {
            return null;
        }
        MessageDigest digest = this.getDigest(operation);
        if (null != digest) {
            DigestUtils.updateDigest(digest, salt);
            DigestUtils.updateDigest(digest, input);
            return Hex.encodeHexString(digest.digest());
        } else {
            byte[] data;
            data = new byte[input.available()];
            input.read(data);
            Base64 base64 = new Base64();
            return base64.encodeAsString(data);
        }
    }

    public String base64Decrypt(String text) throws UnsupportedEncodingException {
        if (org.springframework.util.StringUtils.isEmpty(text)) {
            return null;
        }
        return new String(this.decode(text), "utf-8");
    }

    public void base64Decrypt(String str, OutputStream out) {
        try {
            out.write(this.decode(str));
        } catch (IOException e) {
            log.error(EXPF.getExceptionMsg(e));
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error(EXPF.getExceptionMsg(e));
                }
            }
        }
    }

    private byte[] decode(String str) {
        Base64 base64 = new Base64();
        return base64.decode(str);
    }

    public String decrypt(String value) {
        try {
            return new String(decode(value), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    public String decrypt(String value, String chart) {
        try {
            return new String(decode(value), chart);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public enum OPERATION {
        MD2("md2"), MD5("md5"), SHA1("sha1"), SHA256("sha256"), SHA384("sha384"), SHA512("sha512"), BASE64("base64");

        private String operation;

        private OPERATION(String operation) {
            this.operation = operation;
        }

        public String toString() {
            return operation;
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        EncryptorUtility encryptorUtility = new EncryptorUtility("4NCMAIfiXsjB9OrG");
        String pwd = "eyJtaWQiOiI5MTAwMzk4In0=";
        String str = encryptorUtility.base64Decrypt(pwd);
        System.out.println(str);
    }
}

