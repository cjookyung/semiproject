package org.example.semiproject.member.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String userid;
    private final String password;
    private final String name;
    private final String email;
    private final LocalDateTime regdate;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String userid, String password, String name, String email, LocalDateTime regdate, Collection<? extends GrantedAuthority> authorities) {
        this.userid = userid;
        this.password = password;
        this.name = name;
        this.email = email;
        this.regdate = regdate;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return userid;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
