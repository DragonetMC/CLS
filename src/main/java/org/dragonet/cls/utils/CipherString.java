/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details.
 *
 * @author The Dragonet Team
 */
package org.dragonet.cls.utils;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Epic
 */
public class CipherString {

    private static final String ALGORITHM = "AES";
    private final String cipher_key;

    public CipherString(String cipher_key) {
        this.cipher_key = cipher_key;
    }

    public String Encrypt(String input) {

        String encrypted = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(input.getBytes());
            encrypted = new BASE64Encoder().encode(encValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public String Decrypt(String encrypted) {

        String clean = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encrypted);
            byte[] decValue = c.doFinal(decordedValue);
            clean = new String(decValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clean;
    }

    private Key generateKey() throws Exception {
        return new SecretKeySpec(this.cipher_key.getBytes(), ALGORITHM);
    }
}
