package com.team2.mosoo_backend.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
    NONE("NONE");
//    GOOGLE("GOOGLE");
    private final String provider;
}
