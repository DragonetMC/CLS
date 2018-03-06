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
package org.dragonet.cls.requests;

/**
 *
 * @author Epic
 */
public class CLSLinkRequest {

    private String pin = "";
    private String xbxlname = "";
    private String user = "";
    private String pass = "";
    private Boolean save = false;
    private String email = "";

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @return the xbxlname
     */
    public String getXbxlname() {
        return xbxlname;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @return the save
     */
    public Boolean getSave() {
        return save;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
}
