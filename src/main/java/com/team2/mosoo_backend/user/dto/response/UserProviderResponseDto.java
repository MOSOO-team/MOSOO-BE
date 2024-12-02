package com.team2.mosoo_backend.user.dto.response;

import com.team2.mosoo_backend.user.entity.Provider;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProviderResponseDto {
    private Provider provider;
}
