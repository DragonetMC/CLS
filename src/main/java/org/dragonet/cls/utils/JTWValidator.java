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
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Epic
 */
@Component
public class JTWValidator {

    private final String mojangPubliKey;
    private final KeyFactory ellipticCurveKeyFactory;

    public JTWValidator() throws NullPointerException, IOException, NoSuchAlgorithmException {
        //load the mojang public key
        Resource res = new ClassPathResource("/mojang_root_public.key", this.getClass());
        InputStream inputStream = res.getInputStream();
        File file = File.createTempFile("mojang_root_public", ".key");
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

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
