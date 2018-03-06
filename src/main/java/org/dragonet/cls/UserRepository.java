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

import org.dragonet.cls.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Epic
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.xbox_xuid = :xbox_xuid AND u.xbox_identity = :xbox_identity AND u.xbox_display_name = :xbox_display_name")
    User findByXBoxLiveProfile(@Param("xbox_xuid") String xbox_xuid, @Param("xbox_identity") String xbox_identity, @Param("xbox_display_name") String xbox_display_name);

    @Query("SELECT u FROM User u WHERE u.mojang_uuid = :mojang_uuid")
    User findByUUID(@Param("mojang_uuid") String mojang_uuid);

    @Query("SELECT u FROM User u WHERE u.pin = :pin")
    User findByPin(@Param("pin") String pin);

    @Query("SELECT u FROM User u WHERE u.mojang_username = :mojang_username AND u.mojang_pass = :mojang_pass")
    User findByCredentials(@Param("mojang_username") String mojang_username, @Param("mojang_pass") String mojang_pass);

    @Query("SELECT u FROM User u WHERE u.mojang_uuid = :mojang_uuid")
    User findByMojangUUID(@Param("mojang_uuid") String mojang_uuid);

    @Query("SELECT u FROM User u WHERE u.unlink_token = :unlink_token")
    User findByUnlinkToken(@Param("unlink_token") String unlink_token);
}
