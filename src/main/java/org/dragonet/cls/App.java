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
package org.dragonet.cls;

import org.dragonet.cls.repository.UserRepository;
import org.dragonet.cls.utils.JTWValidator;
import com.google.gson.Gson;
import org.dragonet.cls.controllers.APIController;
import org.dragonet.cls.utils.CipherString;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    // CONFIG
    private String clientToken = "cb341093-6711-4ddc-9751-47727c57b188"; //this is an example TODO generate a new one stored in config
    private String passwordCipher = "Bar12345Bar12345Bar12345Bar12345"; //this is an example TODO generate a new one stored in config
    private int pinExpireTime = 5; //max usage time for the pin code

    private final Gson gson = new Gson();
    private final CipherString cipherString = new CipherString(passwordCipher);

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public App() {
        instance = this;
//        System.out.println("ENV :");
//        for (String prop : System.getenv().keySet()) {
//            System.out.println(prop + "=" + System.getenv().get(prop));
//        }
//        System.out.println("properties :");
//        for (String prop : System.getProperties().stringPropertyNames()) {
//            System.out.println(prop + "=" + System.getProperties().get(prop));
//        }
//        System.out.println("ENV :");
//        for (String prop : System.getenv().keySet()) {
//            System.out.println(prop + "=" + System.getenv().get(prop));
//        }
//        System.out.println("properties :");
//        for (String prop : System.getProperties().stringPropertyNames()) {
//            System.out.println(prop + "=" + System.getProperties().get(prop));
//        }
    }

    @Autowired
    private JTWValidator jtwValidator;

    @Autowired
    private APIController apiController;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    public Gson getGson() {
        return gson;
    }

    /**
     * @return the JTWValidator
     */
    public JTWValidator getJTWValidator() {
        return jtwValidator;
    }

    /**
     * @return the clientToken
     */
    public String getClientToken() {
        return clientToken;
    }

    /**
     * @return the userRepository
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * @return the cipherString
     */
    public CipherString getCipherString() {
        return cipherString;
    }

    /**
     * @return the pinExpireTime
     */
    public int getPinExpireTime() {
        return pinExpireTime;
    }

}
