package com.example.semiproject.member.domain.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {
    private String userid;
    private String passwd;

}
