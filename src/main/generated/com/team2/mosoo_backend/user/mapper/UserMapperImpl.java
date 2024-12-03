package com.team2.mosoo_backend.user.mapper;

import com.team2.mosoo_backend.user.dto.UserInfoDto;
import com.team2.mosoo_backend.user.dto.UserReqeustDto;
import com.team2.mosoo_backend.user.dto.UserResponseDto;
import com.team2.mosoo_backend.user.entity.UserInfo;
import com.team2.mosoo_backend.user.entity.Users;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-03T13:01:36+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public Users requestToUser(UserReqeustDto userReqeustDto) {
        if ( userReqeustDto == null ) {
            return null;
        }

        Users.UsersBuilder<?, ?> users = Users.builder();

        users.password( encryptPassword( userReqeustDto.getPassword() ) );
        users.email( userReqeustDto.getEmail() );
        users.fullName( userReqeustDto.getFullName() );

        return users.build();
    }

    @Override
    public UserResponseDto userToResponse(Users users) {
        if ( users == null ) {
            return null;
        }

        UserResponseDto.UserResponseDtoBuilder userResponseDto = UserResponseDto.builder();

        if ( users.getId() != null ) {
            userResponseDto.id( users.getId() );
        }
        userResponseDto.email( users.getEmail() );
        userResponseDto.fullName( users.getFullName() );
        userResponseDto.createdAt( users.getCreatedAt() );

        return userResponseDto.build();
    }

    @Override
    public UserInfoDto userInfoToDto(UserInfo userInfo) {
        if ( userInfo == null ) {
            return null;
        }

        UserInfoDto.UserInfoDtoBuilder userInfoDto = UserInfoDto.builder();

        userInfoDto.id( userInfo.getId() );
        userInfoDto.address( userInfo.getAddress() );
        userInfoDto.createdAt( userInfo.getCreatedAt() );
        userInfoDto.updatedAt( userInfo.getUpdatedAt() );

        return userInfoDto.build();
    }

    @Override
    public List<UserResponseDto> toDtoList(List<Users> usersList) {
        if ( usersList == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( usersList.size() );
        for ( Users users : usersList ) {
            list.add( userToResponse( users ) );
        }

        return list;
    }
}
