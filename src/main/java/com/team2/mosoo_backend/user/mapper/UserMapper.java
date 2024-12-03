package com.team2.mosoo_backend.user.mapper;



import com.team2.mosoo_backend.user.dto.UserInfoDto;
import com.team2.mosoo_backend.user.dto.UserReqeustDto;
import com.team2.mosoo_backend.user.dto.UserResponseDto;
import com.team2.mosoo_backend.user.entity.User;
import com.team2.mosoo_backend.user.entity.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", source = "password", qualifiedByName = "encryptPassword")
    User requestToUser(UserReqeustDto userReqeustDto);

    UserResponseDto userToResponse(User user);

    UserInfoDto userInfoToDto(UserInfo userInfo);

    List<UserResponseDto> toDtoList(List<User> userList);

    @Named("encryptPassword") // 2
    default String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
