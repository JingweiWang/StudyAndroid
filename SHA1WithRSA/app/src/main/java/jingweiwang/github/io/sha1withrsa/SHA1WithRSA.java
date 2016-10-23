package jingweiwang.github.io.sha1withrsa;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by wangjingwei on 2016/10/23.
 */

public class SHA1WithRSA {
    private static Signature sign = null;
    private static PrivateKey privateKey;

    private SHA1WithRSA() {
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * @param context  the activity context
     * @param posttemp the String for sign with SHA1WithRSA
     * @return signed string showed by base64
     */
    public static String getBase64(Context context, String posttemp) {
        /**
         * take private.pem to pkcs#8 by openssl to the temp
         */
        String temp = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open("key/private_key_pkcs8.pem"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
            temp = result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SHA1WithRSA_to_BASE64", e.getMessage());
        }
        String pkcs8 = temp.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] bytetemp = null;
        try {
            bytetemp = Base64.decode(pkcs8, Base64.DEFAULT);
            privateKey = getPrivateKey(bytetemp);
            sign = Signature.getInstance("SHA1WithRSA");
            sign.initSign(privateKey);
            sign.update(posttemp.getBytes());
            byte[] data = sign.sign();
            return Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "").replace("\t", "").replace(" ", "").trim();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SHA1WithRSA_to_BASE64", e.getMessage());
        }
        return "error";
    }
}
