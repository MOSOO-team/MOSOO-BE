package com.team2.mosoo_backend.chatting.dto;

import lombok.Data;

@Data
public class ChatRoomOpponentInfoResponseDto {

    private boolean isOpponentGosu; // 상대방의 고수 여부

    private String fullName;    // 상대 이름
    private String email;       // 상대 이메일

    // 상대가 고수일 때 사용 필드
    private String businessName;    // 사업자명
    private String businessNumber;  // 사업자 번호
    private String gosuInfoAddress; // 기술 제공 주소
    private String gosuInfoPhone;   // 고수 전화번호
    private String categoryName;    // 기술 제공 카테고리


    public ChatRoomOpponentInfoResponseDto(String fullName, String email) {
        this.isOpponentGosu = false;
        this.fullName = fullName;
        this.email = email;
    }

    public ChatRoomOpponentInfoResponseDto(String businessName, String businessNumber, String gosuInfoAddress, String gosuInfoPhone, String categoryName) {
        this.isOpponentGosu = true;
        this.businessName = businessName;
        this.businessNumber = businessNumber;
        this.gosuInfoAddress = gosuInfoAddress;
        this.gosuInfoPhone = gosuInfoPhone;
        this.categoryName = categoryName;
    }
}
