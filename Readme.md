# Central Login Server

A login manager for linking Xbox Live and Mojang accounts, for DragonProxy

It's a draft, and i just learn Sping, so don"t be too rude in comments / issues.

# Build the image
```./docker.sh```

# Start the conainer
```
docker run --rm -ti -p 8080:8080 dragonet/cls:<version>
```

# Environement variables (override configuration)
 - MOTD the message in ping status
 - MAX_PLAYERS proxy max players
 - TARGET_SERVER minecraft server to point (default mc.hypixel.net:25565)
 - AUTH_MODE proxy auth mode (online, offline, cls)
 - VERIFY check if XboxLive accounts are signed
 - CLS_SERVER the CLS server to contact in CLS mode
 - THREADS thread pool size
 - SENTRY_CLIENT_KEY the sentry DSN

# Volumes
 - /home/proxy/logs extrenal logs dir
 - /home/proxy/config.yml override for proxy config file

## TODO
 - better error handling
 - better unlink process (via mail)
 - properly validate login chain with the cert
 - clean / organize
 - config via file/env