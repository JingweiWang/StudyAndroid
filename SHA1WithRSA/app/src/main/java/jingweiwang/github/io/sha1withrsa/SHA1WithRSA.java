package jingweiwang.github.io.sha1withrsa;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by wangjingwei on 2016/10/23.
 */

public class SHA1WithRSA {
    private SHA1WithRSA() {
    }

    private static String readAssetsKey(Context context, String assetsPath) throws IOException {
        InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(assetsPath));
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line;
        String result = "";
        while ((line = bufReader.readLine()) != null) {
            result += line;
        }
        return result;

    }

    private static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }


    /**
     * 获取字符串的 Base64 格式的 SHA1WithRSA 签名
     *
     * @param context                 activity context
     * @param assetsPathForPrivateKey such as "key/private_key_pkcs8.pem", pkcs#8 format
     * @param oriStr                  String for sign with SHA1WithRSA
     * @return signed string by base64
     */
    public static String signWithBase64(Context context, String assetsPathForPrivateKey, String oriStr) {
        try {
            String pkcs8 = readAssetsKey(context, assetsPathForPrivateKey)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            byte[] keyBytes = Base64.decode(pkcs8, Base64.DEFAULT);
            PrivateKey privateKey = getPrivateKey(keyBytes);
            Signature sign = Signature.getInstance("SHA1WithRSA");
            sign.initSign(privateKey);
            sign.update(oriStr.getBytes());
            byte[] data = sign.sign();
            return Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "").replace("\t", "").replace(" ", "").trim();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeySpecException | InvalidKeyException | IOException e) {
            e.printStackTrace();
            Log.e("SHA1WithRSA_to_BASE64", e.getMessage());
        }
        return "error";
    }

    /**
     * 验证签名是否与原字符串匹配
     *
     * @param context                activity context
     * @param assetsPathForPublicKey such as "key/public_key.pem"
     * @param oriStr                 String for verify with SHA1WithRSA
     * @param signedStr              signed string by base64
     * @return true if verify pass
     */
    public static boolean verify(Context context, String assetsPathForPublicKey, String oriStr, String signedStr) {
        try {
            String publicKeyStr = readAssetsKey(context, assetsPathForPublicKey)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");
            byte[] keyBytes = Base64.decode(publicKeyStr, Base64.DEFAULT);
            PublicKey publicKey = getPublicKey(keyBytes);
            Signature sign = Signature.getInstance("SHA1WithRSA");
            sign.initVerify(publicKey);
            sign.update(oriStr.getBytes());
            return sign.verify(Base64.decode(signedStr, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeySpecException | InvalidKeyException | IOException e) {
            e.printStackTrace();
            Log.e("SHA1WithRSA_to_BASE64", e.getMessage());
        }
        return false;
    }
}
