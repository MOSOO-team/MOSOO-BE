package com.team2.mosoo_backend.user.mapper;



import com.team2.mosoo_backend.user.dto.GosuRequestDto;
import com.team2.mosoo_backend.user.dto.UserInfoDto;
import com.team2.mosoo_backend.user.dto.UserRequestDto;
import com.team2.mosoo_backend.user.dto.UserResponseDto;
import com.team2.mosoo_backend.user.entity.Gosu;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.entity.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", source = "password", qualifiedByName = "encryptPassword")
    Users requestToUser(UserRequestDto userRequestDto);

    UserResponseDto userToResponse(Users users);

    @Mapping(source = "users", target = "userId")
    UserInfoDto userInfoToDto(UserInfo userInfo);

    @Mapping(target = "category", ignore = true)
    Gosu requestToGosu(GosuRequestDto gosuRequestDto);

    default Long mapUsersToLong(Users users) { return users != null ? users.getId() : null; }

    List<UserResponseDto> toDtoList(List<Users> usersList);

    @Named("encryptPassword") // 2
    default String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
