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
package org.dragonet.cls.controllers;

import org.dragonet.cls.utils.XboxLiveIdentity;
import org.dragonet.cls.entities.User;
import org.dragonet.cls.requests.*;
import org.dragonet.cls.responses.*;
import org.springframework.http.MediaType;
import org.dragonet.cls.App;
import org.dragonet.cls.utils.MailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {

    @PostMapping(
            path = "/api/v1/authenticate",
            headers = "Accept=application/json",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public String authenticate(@RequestBody CLSAuthenticateRequest postPayload) {
        XboxLiveIdentity identity = new XboxLiveIdentity(postPayload);
        if (identity.validate()) { //identity is valid
            User user = App.getInstance().getUserRepository().findByXBoxLiveProfile(identity.getXuid(), identity.getIdentity(), identity.getUsername());
            if (user != null) { //the user exist
                if (user.hasTokens()) { //user already OK, checking data
                    //TODO authserver.mojang.com/validate (if false, resend pin or re authenticate)
                    if (!user.validateTokens()) { // validation fail
                        if (user.hasCredentials()) {
                            //mojan authenticate, refresh access token
                            if (user.authenticate(true)) {
                                App.getInstance().getUserRepository().save(user);
                                return new CLSAuthenticateResponse(user).toString();
                            } else {
                                user.resetCredentials();
                                user.resetTokens();
                                App.getInstance().getUserRepository().save(user);
                                return new CLSErrorResponse("fail_to_login").toString();
                            }
                        } else {
                            user.resetCredentials();
                            user.resetTokens();
                            user.generatePin(4);
                            App.getInstance().getUserRepository().save(user);
                            return new CLSPinResponse("pre_register_done", user.getPin()).toString();
                        }
                    } else {
                        App.getInstance().getUserRepository().save(user);
                        return new CLSAuthenticateResponse(user).toString();
                    }
                } else if (user.isPinOutdated()) {
                    user.resetCredentials();
                    user.resetTokens();
                    user.generatePin(4);
                    App.getInstance().getUserRepository().save(user);
                    return new CLSPinResponse("pre_register_done", user.getPin()).toString();
                } else {
                    return new CLSPinResponse("need_link", user.getPin()).toString();
                }
            } else { //create the user
                user = new User(identity);
                user.generatePin(4);
                App.getInstance().getUserRepository().save(user);
                return new CLSPinResponse("pre_register_done", user.getPin()).toString();
            }
        }
        return new CLSErrorResponse("no_profile_found").toString();
    }

    @PostMapping(
            path = "/api/v1/refresh",
            headers = "Accept=application/json",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public String refresh(@RequestBody CLSRefreshRequest postPayload) {
        User user = App.getInstance().getUserRepository().findByUUID(postPayload.getUuid());
        if (user != null) {
            user.setMojangAccessToken(postPayload.getAccessToken());
            App.getInstance().getUserRepository().save(user);
            return new CLSSuccessResponse("profile_refreshed").toString();
        }
        return new CLSErrorResponse("no_profile_found").toString();
    }

    @RequestMapping(
            value = "/api/v1/pin",
            method = RequestMethod.GET,
            headers = "Accept=application/json",
            produces = {"application/json"}
    )
    public String pin(@RequestParam("pin") String pin) {
        User user = App.getInstance().getUserRepository().findByPin(pin);
        if (user != null) {
            if (user.hasCredentials()) {
                return new CLSErrorResponse("already_registered").toString();
            } else if (user.isPinOutdated()) {
                return new CLSErrorResponse("expired_link").toString();
            } else {
                return new CLSProfileResponse(user).toString();
            }
        }
        return new CLSErrorResponse("no_profile_found").toString();
    }

    @PostMapping(
            path = "/api/v1/link",
            headers = "Accept=application/json",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public String link(@RequestBody CLSLinkRequest postPayload) {
        if (!postPayload.getUser().isEmpty() && !postPayload.getPass().isEmpty() && !postPayload.getPin().isEmpty() && !postPayload.getXbxlname().isEmpty()) {
            User user = App.getInstance().getUserRepository().findByPin(postPayload.getPin());
            if (user != null) {
                if (user.getXboxDisplayName().equals(postPayload.getXbxlname())) {
                    if (!postPayload.getEmail().isEmpty() && !MailSender.isEmailValid(postPayload.getEmail())) {
                        return new CLSErrorResponse("bad_mail").toString();
                    }
                    user.setMojangPass(App.getInstance().getCipherString().Encrypt(postPayload.getPass()));
                    user.setMojangUsername(App.getInstance().getCipherString().Encrypt(postPayload.getUser()));
                    if (user.authenticate(postPayload.getSave())) {
                        if (!postPayload.getEmail().isEmpty()) {
                            user.setUnlinkEmail(postPayload.getEmail());
                            MailSender.sendUnlinkMail(user.getUnlinkEmail(), user.getUnlinkToken());
                        }
                        App.getInstance().getUserRepository().save(user);
                        return new CLSLinkedResponse(user).toString();
                    }
                    return new CLSErrorResponse("fail_to_login").toString();
                }
            }
        }
        return new CLSErrorResponse("no_profile_found").toString();
    }

    @RequestMapping(value = "/api/v1/unlink",
            method = RequestMethod.GET,
            headers = "Accept=application/json",
            produces = {"application/json"}
    )
    public String unLinkToken(@RequestParam("token") String token) {
        User user = App.getInstance().getUserRepository().findByUnlinkToken(token);
        if (user != null) {
            user.resetTokens();
            user.resetCredentials();
            user.resetUnlink();
            App.getInstance().getUserRepository().save(user);
            return new CLSSuccessResponse("accounts_unlinked").toString();
        }
        return new CLSErrorResponse("no_profile_found").toString();
    }

    @PostMapping(path = "/api/v1/unlink",
            headers = "Accept=application/json",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public String unLinkAccount(@RequestBody CLSUnlinkRequest postPayload) {
        User user = App.getInstance().getUserRepository().findByCredentials(
                App.getInstance().getCipherString().Encrypt(postPayload.getUser()),
                App.getInstance().getCipherString().Encrypt(postPayload.getPass())
        );
        if (user == null) { //try login  + real UUID
            User tempUser = new User();
            tempUser.setMojangUsername(App.getInstance().getCipherString().Encrypt(postPayload.getUser()));
            tempUser.setMojangPass(App.getInstance().getCipherString().Encrypt(postPayload.getPass()));
            if (tempUser.authenticate(false)) {
                user = App.getInstance().getUserRepository().findByUUID(tempUser.getMojangUuid());
            }
        }
        if (user != null) {
            user.resetTokens();
            user.resetCredentials();
            user.resetUnlink();
            App.getInstance().getUserRepository().save(user);
            return new CLSSuccessResponse("accounts_unlinked").toString();
        }
        return new CLSErrorResponse("no_profile_found").toString();
    }
}
