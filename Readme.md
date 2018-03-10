# Central Login Server

A login manager for linking Xbox Live and Mojang accounts, for DragonProxy

It's a draft, and i just learn Spring, so don't be too rude in comments / issues.

# Features
 - allow Minecraft XboxLive -> Mojang account linking

# Build the image
```./docker.sh```

# Start the container
```
docker run --rm \
 -p 8080:8080 \
 -e CLIENT_TOKEN="cb341093-6711-4ddc-9751-47727c57b188" \
 -e PASSWORD_CIPHER="Bar12345Bar12345Bar12345Bar12345" \
 -e PIN_EXPIRE_TIME=5 \
 -e MYSQL_HOST=mysql \
 -e MYSQL_PORT=3306 \
 -e MYSQL_USER=root \
 -e MYSQL_PASS=root \
 -e MYSQL_DATA=dragon_proxy \
 --link mysql:mysql \
 -ti dragonet/cls:1.0.0
```
If you start the CLS without params, it will use default config, and generate a file in root project dir.
If you want to override default config without env vaiables, copy the default config, and tweak.

# Environement variables (override configuration)
 - CLIENT_TOKEN the internal session client token (must be changed for each different setups)
 - PASSWORD_CIPHER the key for cipher passwords in database (must be changed for each different setups)
 - PIN_EXPIRE_TIME max time for validate a pin code, in minute
 - MYSQL_* mysql configuration

## TODO
 - better error handling
 - TLS
 - send unlink mail