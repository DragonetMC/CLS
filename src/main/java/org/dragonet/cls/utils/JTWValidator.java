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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Epic
 */
public class JTWValidator {

    private final String mojangPubliKey;
    private final KeyFactory ellipticCurveKeyFactory;

    public JTWValidator() throws IOException, NoSuchAlgorithmException {
        //load the mojang public key
        ClassLoader classLoader = JTWValidator.class.getClassLoader();
        File file = new File(classLoader.getResource("mojang_root_public.key").getFile());

        //Read File Content
        String content = new String(Files.readAllBytes(file.toPath()));
        mojangPubliKey = content;

        // Java 7 fully implements elliptic curve crypto
        ellipticCurveKeyFactory = KeyFactory.getInstance("EC");
    }

    public String getMojangPubliKey() {
        return mojangPubliKey;
    }

    public KeyFactory getEllipticCurveKeyFactory() {
        return ellipticCurveKeyFactory;
    }

}
