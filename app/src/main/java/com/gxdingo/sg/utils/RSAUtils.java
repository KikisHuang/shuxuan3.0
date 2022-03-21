package com.gxdingo.sg.utils;


import android.os.Build;
import android.util.Base64;

import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import okio.Utf8;

import static com.blankj.utilcode.util.EncodeUtils.base64Decode;
import static com.blankj.utilcode.util.EncodeUtils.base64Encode;
import static com.blankj.utilcode.util.EncodeUtils.base64Encode2String;
import static com.blankj.utilcode.util.ResourceUtils.readAssets2String;
import static java.util.Base64.getMimeDecoder;

public class RSAUtils {

    /**
     * 获取私钥路径
     *
     * @return
     */
    public static String getPrivateKeyPath() {

        String pkey = readAssets2String("rsa_private_key_pkcs8.pem").replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        return pkey;

    }

    /**
     * 获取公钥路径
     *
     * @return
     */
    public static String getPublicKeyPath() {

        String publicKey = readAssets2String("rsa_public_key_2048.pem").replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        return publicKey;

    }

}
