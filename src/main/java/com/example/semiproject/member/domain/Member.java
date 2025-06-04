package com.example.semiproject.member.domain;


import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    private int mno;
    private String userid;
    private String passwd;
    private String name;
    private String email;
    private LocalDateTime regdate;

}
