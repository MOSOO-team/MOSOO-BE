package com.team2.mosoo_backend.user.service;


import com.team2.mosoo_backend.config.SecurityUtil;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.user.dto.UserListResponse;
import com.team2.mosoo_backend.user.dto.UserResponseDto;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.entity.UserInfo;
import com.team2.mosoo_backend.user.mapper.UserMapper;
import com.team2.mosoo_backend.user.repository.UserInfoRepository;
import com.team2.mosoo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final SecurityUtil securityUtil;

    // 맴버 정보 조회
    public UserResponseDto getMyInfoBySecurity() {
       // 사용자 ID를 가져와서 정보 조회하기
        Users me = userRepository.findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 멤버 상세 정보 조회하기
        UserInfo userInfo = userInfoRepository.findByUsersId(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // UserResponseDto 생성
        UserResponseDto responseDto = userMapper.userToResponse(me);
        responseDto.setUserInfoDto(userMapper.userInfoToDto(userInfo));

        return responseDto;
    }

    @Transactional
    public UserResponseDto changeMemberUserName(String fullName) {
        Users users = userRepository.findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (userRepository.findByFullName(fullName).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
        else {
            users.setFullName(fullName);
        }
        return userMapper.userToResponse(userRepository.save(users));
    }

    // 유저 비밀번호 변경
    @Transactional
    public UserResponseDto changeMemberPassword(String exPassword, String newPassword) {
        Users users = userRepository.findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(exPassword, users.getPassword())) {
            throw new CustomException(ErrorCode.AUTH_CODE_EXTENSION);
//            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        users.setPassword(passwordEncoder.encode((newPassword)));
        return userMapper.userToResponse(userRepository.save(users));
    }


    @Transactional
    public UserResponseDto deleteMember(){
        Long memberId = securityUtil.getCurrentMemberId();

//        List<Order> orders = orderRepository.findByMemberId(memberId);
//        if(!orders.isEmpty()){
//            orderRepository.deleteAll(orders);
//        }

        Users users = userRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(users);
        return userMapper.userToResponse(users);
    }

    public Users findById(Long memberId) {
        return userRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    // admin
    public UserListResponse getMemberList(int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").ascending());

        Page<Users> memberList = userRepository.findAll(pageRequest);

        List<UserResponseDto> members = new ArrayList<>();

        for(Users users : memberList) {
            members.add(userMapper.userToResponse(users));
        }
        // 총 페이지 수
        int totalPages = (memberList.getTotalPages()==0 ? 1 : memberList.getTotalPages());

        return new UserListResponse(members, totalPages);
    }

    @Transactional
    public void deleteMemberByMemberId(Long memberId) {
//        List<Order> orders = orderRepository.findByMemberId(memberId);
//        if(!orders.isEmpty()){
//            orderRepository.deleteAll(orders);
//        }

        Users users = userRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(users);
    }

    public UserListResponse getIsDeleteMembers(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        Page<Users> memberList = userRepository.findAllByIsDeleteTrue(pageRequest);

        List<UserResponseDto> members = new ArrayList<>();

        for(Users users : memberList) {
            members.add(userMapper.userToResponse(users));
        }
        // 총 페이지 수
        int totalPages = (memberList.getTotalPages()==0 ? 1 : memberList.getTotalPages());

        return new UserListResponse(members, totalPages);
    }



}