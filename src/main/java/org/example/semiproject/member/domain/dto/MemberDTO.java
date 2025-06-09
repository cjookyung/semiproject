package org.example.semiproject.member.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDTO {

    private String userid;
    private String passwd;
    private String name;
    private String email;

}
