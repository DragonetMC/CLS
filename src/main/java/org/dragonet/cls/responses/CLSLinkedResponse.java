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

import org.dragonet.cls.entities.User;

/**
 *
 * @author Epic
 */
public class CLSLinkedResponse extends CLSResponse {

    public CLSLinkedResponse(User user) {
        this.success = "accounts_linked";
        this.unlink_token = user.getUnlinkToken();
    }
}
