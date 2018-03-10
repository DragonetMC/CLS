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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import org.dragonet.cls.controllers.APIController;
import org.dragonet.cls.utils.CipherString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class App {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // CONFIG
    private String clientToken; //this is an example TODO generate a new one stored in config
    private String passwordCipher; //this is an example TODO generate a new one stored in config
    private int pinExpireTime = 5; //max usage time for the pin code

    private final Gson gson = new Gson();
    private final CipherString cipherString;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public App() {
        instance = this;
        // Config loading / creating default / creating from env
        Resource config = new PathResource("application.properties");
        if (config.exists()) {
            try {
                logger.info("Config file \"application.properties\" found, loading config !");
                Properties properties = new Properties();
                properties.load(config.getInputStream());
                System.getProperties().putAll(properties);
                clientToken = properties.getProperty("app.configuration.clientToken");
                passwordCipher = properties.getProperty("app.configuration.passwordCipher");

                logger.info("Using properties file for CLIENT_TOKEN : " + clientToken);
                logger.info("Using properties file for PASSWORD_CIPHER : " + passwordCipher);
                try {
                    pinExpireTime = Integer.parseInt(properties.getProperty("app.configuration.pinExpireTime"));
                    logger.info("Using properties file for PIN_EXPIRE_TIME : " + pinExpireTime);
                } catch (NumberFormatException ex) {
                    logger.warn("Unable to get pinExpireTime from properties file, " + properties.getProperty("app.configuration.pinExpireTime") + " is not a number !");
                }
            } catch (IOException ex) {
                logger.warn("Unable to read application.properties");
                ex.printStackTrace();
            }
        } else { //load from env
            if (System.getenv().containsKey("CLIENT_TOKEN")) {
                clientToken = System.getenv().get("CLIENT_TOKEN");
                logger.info("Using env vaiable for CLIENT_TOKEN : " + clientToken);
            } else {
                clientToken = UUID.randomUUID().toString();
                logger.info("Generate new CLIENT_TOKEN : " + clientToken);
            }
            if (System.getenv().containsKey("PASSWORD_CIPHER")) {
                passwordCipher = System.getenv().get("PASSWORD_CIPHER");
                logger.info("Using env vaiable for PASSWORD_CIPHER : " + passwordCipher);
            } else {
                passwordCipher = UUID.randomUUID().toString();
                logger.info("Generate new PASSWORD_CIPHER : " + passwordCipher);
            }
            if (System.getenv().containsKey("PIN_EXPIRE_TIME"))
                try {
                    pinExpireTime = Integer.parseInt(System.getenv().get("PIN_EXPIRE_TIME"));
                    logger.info("Using env vaiable for PIN_EXPIRE_TIME : " + pinExpireTime);
                } catch (NumberFormatException ex) {
                    logger.warn("Unable to get PIN_EXPIRE_TIME from env, " + System.getenv().get("PIN_EXPIRE_TIME") + " is not a number !");
                }

            //fill config
            Properties properties = new Properties();
            try {
                Resource resourceConfig = new ClassPathResource("/application.properties", this.getClass());
                properties.load(resourceConfig.getInputStream());
            } catch (IOException ex) {
                logger.warn("Unable to save application.properties");
                ex.printStackTrace();
            }

            // Mysql
            if (System.getenv().containsKey("MYSQL_HOST") && System.getenv().containsKey("MYSQL_PORT") && System.getenv().containsKey("MYSQL_DATA"))
                properties.put("spring.datasource.url", "jdbc:mysql://" + System.getenv().get("MYSQL_HOST") + ":" + System.getenv().get("MYSQL_PORT") + "/" + System.getenv().get("MYSQL_DATA") + "?useSSL=false");
            if (System.getenv().containsKey("MYSQL_USER"))
                properties.put("spring.datasource.username", System.getenv().get("MYSQL_USER"));
            if (System.getenv().containsKey("MYSQL_PASS"))
                properties.put("spring.datasource.password", System.getenv().get("MYSQL_PASS"));

            //other config
            properties.put("app.configuration.clientToken", clientToken);
            properties.put("app.configuration.passwordCipher", passwordCipher);
            properties.put("app.configuration.pinExpireTime", pinExpireTime + "");

            // saving
            OutputStream output = null;
            try {
                output = new FileOutputStream(config.getFilename());
                properties.store(output, "Generated config for Dragonet CLS");
            } catch (FileNotFoundException ex) {
                logger.warn("Unable to save application.properties");
                ex.printStackTrace();
            } catch (IOException ex) {
                logger.warn("Unable to save application.properties");
                ex.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        cipherString = new CipherString(passwordCipher);
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

    /**
     * @return the log
     */
    public Logger getLogger() {
        return logger;
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
