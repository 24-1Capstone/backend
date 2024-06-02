package org.example.user.domain.entity.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.example.reservation.domain.entity.Reservation;
import org.example.user.domain.entity.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Schema(description = "사용자 상세 정보를 위한 도메인 객체")
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity implements UserDetails{ // UserDetails를 상속받아 인증 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // 사용자 이름
    @Column(name = "username", unique = true)
    private String username;

    @Column(nullable = false)
    private String avatarUrl;

    @Column(name = "password")
    private String password;

    @Column(name = "followers_url")
    private String followersUrl;

    @Column(name = "followings_url")
    private String followingsUrl;

    private String provider;

    private String providerId;

    private String accessToken;



    @Builder
    public User(String username, String avatarUrl, String password, String followersUrl, String followingsUrl, String provider, String providerId, String accessToken) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.password = password;
        this.followersUrl = followersUrl;
        this.followingsUrl = followingsUrl;
        this.provider = provider;
        this.providerId = providerId;
        this.accessToken = accessToken;
    }


    //사용자 이름 변경
    public User update(String username) {
        this.username = username;
        return this;
    }

    public User addAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자의 id를 반환(고유한 값)
    @Override
    public String getUsername() {
        return username;
    }

    // 사용자의 패스워드 반환
    @Override
    public String getPassword() {
        return password;
    }


    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료 되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; //true -> 만료되지 않음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }
}
