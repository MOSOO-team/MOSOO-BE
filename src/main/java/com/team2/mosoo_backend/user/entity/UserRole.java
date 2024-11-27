package com.team2.mosoo_backend.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("User");
//    ADMIN("admin");

    private final String roleName;

}
