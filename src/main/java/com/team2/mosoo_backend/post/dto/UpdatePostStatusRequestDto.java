package com.team2.mosoo_backend.post.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostStatusRequestDto {

    private Long id;

    private String status;

}
