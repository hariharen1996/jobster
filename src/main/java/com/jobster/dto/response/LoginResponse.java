package com.jobster.dto.response;

import com.jobster.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String token;
    private Role role;
}
