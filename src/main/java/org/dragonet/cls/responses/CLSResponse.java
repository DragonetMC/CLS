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
package org.dragonet.cls.responses;

import java.io.Serializable;
import org.dragonet.cls.App;

/**
 *
 * @author Epic
 */
public abstract class CLSResponse implements Serializable {

    public static String ERROR_1 = "Too many login attempts";
    public static String ERROR_2 = "something bad happened with CLS ><";
    public static String ERROR_3 = "Bad data provided";
    public static String ERROR_4 = "already_registered";
    public static String ERROR_5 = "expired_link";
    public static String ERROR_6 = "not_preregistered";
    public static String ERROR_7 = "bad_pin";
    public static String ERROR_8 = "no_pin_provided";
    public static String ERROR_9 = "accounts_unlinked";
    public static String ERROR_10 = "invalid_unlink_token";

    String error = null;
    String success = null;
    String message = null;

    String pin = null;
    String unlink_token = null;

    String xbxlname = null;

    String clientToken = null;
    String accessToken = null;
    String uuid = null;
    String displayName = null;

    @Override
    public String toString() {
        return App.getInstance().getGson().toJson(this);
    }
}
