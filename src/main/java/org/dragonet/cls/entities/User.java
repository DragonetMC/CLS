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
package org.dragonet.cls.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import java.io.Serializable;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.dragonet.cls.App;
import org.dragonet.cls.utils.XboxLiveIdentity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author Epic
 */
@Entity
@Table(name = "dp_users")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(length = 11)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, updatable = false, length = 50)
    @NotBlank
    private String xbox_xuid;

    @Column(nullable = false, updatable = false, length = 36)
    @NotBlank
    private String xbox_identity;

    @Column(nullable = false, updatable = false, length = 50)
    @NotBlank
    private String xbox_display_name;

    @Column(nullable = true, updatable = true, length = 10)
    private String pin;

    @Column(nullable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date pinTime;

    @Column(nullable = true, updatable = true, length = 50)
    private String mojang_username;

    @Column(nullable = true, updatable = true, length = 50)
    private String mojang_pass;

    @Column(nullable = true, updatable = true, length = 36)
    private String mojang_uuid;

    @Column(nullable = true, updatable = true, length = 32)
    private String mojang_userid;

    @Column(nullable = true, updatable = true, length = 50)
    private String mojang_display_name;

    @Column(nullable = true, updatable = true, length = 36)
    private String mojang_client_token;

    @Column(nullable = true, updatable = true, length = 32)
    private String mojang_access_token;

    @Column(nullable = true, updatable = true, length = 40)
    private String unlink_token;

    @Column(nullable = true, updatable = true, length = 60)
    private String unlink_email;

    @Column(nullable = false, updatable = false)
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_activity;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date register_date;

    public User() {
        super();
    }

    public User(XboxLiveIdentity XBoxLiveProfile) {
        this.xbox_xuid = XBoxLiveProfile.getXuid().toString();
        this.xbox_identity = XBoxLiveProfile.getIdentity();
        this.xbox_display_name = XBoxLiveProfile.getUsername();
        this.register_date = Date.from(Instant.now());
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the xbox_xuid
     */
    public String getXboxXuid() {
        return xbox_xuid;
    }

    /**
     * @return the xbox_identity
     */
    public String getXboxIdentity() {
        return xbox_identity;
    }

    /**
     * @return the xbox_display_name
     */
    public String getXboxDisplayName() {
        return xbox_display_name;
    }

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @return the pinTime
     */
    public Date getPinTime() {
        return pinTime;
    }

    /**
     * @param mojang_username the mojang_username to set
     */
    public void setMojangUsername(String mojang_username) {
        this.mojang_username = mojang_username;
    }

    /**
     * @return the mojang_username
     */
    public String getMojangUsername() {
        return mojang_username;
    }

    /**
     * @param mojang_pass the mojang_pass to set
     */
    public void setMojangPass(String mojang_pass) {
        this.mojang_pass = mojang_pass;
    }

    /**
     * @return the mojang_pass
     */
    public String getMojangPass() {
        return mojang_pass;
    }

    /**
     * @return the mojang_uuid
     */
    public String getMojangUuid() {
        return mojang_uuid;
    }

    /**
     * @return the mojang_userid
     */
    public String getMojangUserid() {
        return mojang_userid;
    }

    /**
     * @return the mojang_display_name
     */
    public String getMojangDisplayName() {
        return mojang_display_name;
    }

    /**
     * @return the mojang_client_token
     */
    public String getMojangClientToken() {
        return mojang_client_token;
    }

    /**
     * @param mojang_access_token the mojang_access_token to set
     */
    public void setMojangAccessToken(String mojang_access_token) {
        this.mojang_access_token = mojang_access_token;
    }

    /**
     * @return the mojang_access_token
     */
    public String getMojangAccessToken() {
        return mojang_access_token;
    }

    /**
     * @return the unlink_token
     */
    public String getUnlinkToken() {
        return unlink_token;
    }

    /**
     * @return the unlink_email
     */
    public String getUnlinkEmail() {
        return unlink_email;
    }

    /**
     * @param unlink_email the unlink_email to set
     */
    public void setUnlinkEmail(String unlink_email) {
        this.unlink_email = unlink_email;
    }

    /**
     * @return the last_activity
     */
    public Date getLastActivity() {
        return last_activity;
    }

    /**
     * @return the register_date
     */
    public Date getRegisterDate() {
        return register_date;
    }

    public void generatePin(int lenght) {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < lenght) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            salt.append(CHARS.charAt(index));
        }
        this.pin = salt.toString();
        this.pinTime = Date.from(Instant.now());
    }

    public void generateUnlinkToken(int lenght) {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < lenght) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            salt.append(CHARS.charAt(index));
        }
        this.unlink_token = salt.toString();
    }

    public boolean isPinOutdated() {
        return this.pinTime == null || (Date.from(Instant.now()).after(Date.from(this.pinTime.toInstant().plus(App.getInstance().getPinExpireTime(), ChronoUnit.MINUTES))));
    }

    public boolean hasTokens() {
        return this.mojang_client_token != null && this.mojang_access_token != null && this.mojang_uuid != null && this.mojang_display_name != null;
    }

    public boolean hasCredentials() {
        return this.mojang_username != null && this.mojang_pass != null;
    }

    public void resetTokens() {
        this.mojang_client_token = null;
        this.mojang_access_token = null;
        this.mojang_uuid = null;
        this.mojang_userid = null;
        this.mojang_display_name = null;
    }

    public void resetCredentials() {
        this.mojang_username = null;
        this.mojang_pass = null;
    }

    public void resetUnlink() {
        this.unlink_token = null;
        this.unlink_email = null;
    }

    public boolean authenticate(boolean saveCredentials) {
        try {
            AuthenticationService auth = new AuthenticationService(App.getInstance().getClientToken(), Proxy.NO_PROXY);
            auth.setUsername(App.getInstance().getCipherString().Decrypt(this.mojang_username));
            auth.setPassword(App.getInstance().getCipherString().Decrypt(this.mojang_pass));
            auth.login();
            // thow RequestException here if can't login
            if (this.unlink_token == null)
                generateUnlinkToken(40); //generate the inlink token
            if (!saveCredentials)
                resetCredentials(); //not save credentials
            this.mojang_client_token = auth.getClientToken();
            this.mojang_access_token = auth.getAccessToken();
            this.mojang_userid = auth.getId();
            this.mojang_uuid = auth.getSelectedProfile().getIdAsString();
            this.mojang_display_name = auth.getSelectedProfile().getName();
            this.pin = null;
            this.pinTime = null;
            return true;
        } catch (RequestException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean validateTokens() {
        this.last_activity = Date.from(Instant.now());
        //TODO implements this
        return true;
    }

    public void sendUnlinkMail() {
        
    }
}
