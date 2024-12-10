package com.team2.mosoo_backend.user.dto;

import com.team2.mosoo_backend.user.entity.Authority;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "비밀번호가 비어있습니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상 필요합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$",
            message = "비밀번호는 숫자를 포함한 문자 8자 이상 필요합니다.")
    private String password;

    private String fullName;

    private Authority authority;



    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
