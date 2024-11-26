package com.team2.mosoo_backend.user.domain;

import com.team2.mosoo_backend.common.entity.BaseEntity;
import com.team2.mosoo_backend.user.dto.request.UserUpdateRequestDto;
import com.team2.mosoo_backend.user.dto.response.UsersInfoResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UsersInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id")
    private Long userInfoId;

    @Column(name = "address")
    private String address; //우편번호

    @Column(name = "street_address")
    private String streetAddress; //도로명 주소

    @Column(name = "detailed_address")
    private String detailedAddress; //상세 주소

    @ManyToOne
    @JoinColumn(name = "user_id")   //왜래키 설정
    private Users users;

    public UsersInfoResponseDto toUsersInfoResponseDto() {
        return UsersInfoResponseDto.builder()
                .address(address)
                .streetAddress(streetAddress)
                .detailedAddress(detailedAddress)
                .build();
    }

    public void updateUserInfo(UserUpdateRequestDto userUpdateRequestDto) {
        //DB에 저장된 첫번째 값을 가지고온다.
        UsersInfoResponseDto userInfoDto = userUpdateRequestDto.getAddress().get(0);
        this.address = userInfoDto.getAddress();
        this.streetAddress = userInfoDto.getStreetAddress();
        this.detailedAddress = userInfoDto.getDetailedAddress();
    }
}
