package com.iplay.feastbooking.assistance;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by admin on 2017/10/16.
 */

public class SecurityUtility {

    private static final String CipherMode = "AES/CFB/NoPadding";

    public static final String key = "123456789";

    public static String encrypt(String key, String data) throws Exception {
        try {
                Cipher cipher = Cipher.getInstance(CipherMode);
                SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
                byte[] encrypted = cipher.doFinal(data.getBytes());
                return Base64.encodeToString(encrypted, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
        }
    }

    public static String decrypt(String key, String data) throws Exception {
        try {
            byte[] encrypted1 = Base64.decode(data.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
