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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.dragonet.cls.App;
import org.dragonet.cls.requests.CLSAuthenticateRequest;

/**
 *
 * @author Epic
 */
public class XboxLiveIdentity {

    private final String JTWChain;
    private final String JTWClientData;

    private String xuid;
    private String identity;
    private String username;

    public XboxLiveIdentity(CLSAuthenticateRequest CLSAuthenticateRequest) {
        this.JTWChain = CLSAuthenticateRequest.getChain();
        this.JTWClientData = CLSAuthenticateRequest.getToken();
    }

    public boolean validate() {
        Map<String, List<String>> map = App.getInstance().getGson().fromJson(JTWChain, new TypeToken<Map<String, List<String>>>() {}.getType());

        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty())
            return false;

        DecodedJWT clientJWT = JWT.decode(this.JTWClientData);
        List<DecodedJWT> chainJWTs = new ArrayList<>();

        // Add the JWT tokens to a chain
        for (String token : map.get("chain"))
            chainJWTs.add(JWT.decode(token));
        chainJWTs.add(clientJWT);

        // Check if the public provided key can decode the received chain
        try {
            ECPublicKey prevPublicKey = null;
            for (DecodedJWT jwt : chainJWTs) {

                JsonObject payload = App.getInstance().getGson().fromJson(new String(Base64.getDecoder().decode(jwt.getPayload())), JsonObject.class);
                String encodedPublicKey = null;
                ECPublicKey publicKey = null;

                if (payload.has("identityPublicKey")) {
                    encodedPublicKey = payload.get("identityPublicKey").getAsString();
                    publicKey = (ECPublicKey) App.getInstance().getJTWValidator().getEllipticCurveKeyFactory().generatePublic(
                            new X509EncodedKeySpec(Base64.getDecoder().decode(encodedPublicKey))
                    );
                }

                // Trust the root ca public key and use it to verify the chain
                if (App.getInstance().getJTWValidator().getMojangPubliKey().equals(encodedPublicKey) && payload.has("certificateAuthority")
                        && payload.get("certificateAuthority").getAsBoolean()) {
                    prevPublicKey = publicKey;
                    continue;
                }

                // This will happen if the root ca key we have does not match the one presented by the client chain
                if (prevPublicKey == null)
                    throw new NullPointerException("No trusted public key found in chain, is the client logged in or cracked");

                // Throws a SignatureVerificationException if the verification failed
                Algorithm.ECDSA384(prevPublicKey, null).verify(jwt);

                // Verification was successful since no exception was thrown
                // Set the previous public key to this one so that it can be used
                // to verify the next JWT token in the chain
                prevPublicKey = publicKey;
            }
            // The for loop successfully verified all JWT tokens with no exceptions thrown
            Logger.getLogger(this.getClass().getSimpleName()).info("The LoginPacket has been successfully verified for integrity");

        } catch (SignatureVerificationException | JsonSyntaxException | IllegalArgumentException | NullPointerException | InvalidKeySpecException e) {
            Logger.getLogger(this.getClass().getSimpleName()).info("Failed to verify the integrity of the LoginPacket");
            e.printStackTrace();
            return false;
        }

        // step 2, extract player displayName and identity
        // This is in its own for loop due to the possibility that the chain verification failed
        for (DecodedJWT jwt : chainJWTs) {
            JsonObject payload = App.getInstance().getGson().fromJson(new String(Base64.getDecoder().decode(jwt.getPayload())), JsonObject.class);
            // Get the information we care about - The UUID and display name
            if (payload.has("extraData") && !payload.has("certificateAuthority")) {
                JsonObject extraData = payload.get("extraData").getAsJsonObject();
                if (extraData.has("displayName") && extraData.has("identity") && extraData.has("XUID")) {
                    this.username = extraData.get("displayName").getAsString();
                    this.identity = extraData.get("identity").getAsString();
                    this.xuid = extraData.get("XUID").getAsString();
                    return true;
                }
            }
        }
        return false;
    }

    public String getXuid() {
        return xuid;
    }

    public String getIdentity() {
        return identity;
    }

    public String getUsername() {
        return username;
    }
}
