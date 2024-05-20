package org.example.user.repository.member;

import org.example.user.domain.entity.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // username 사용자 정보를 가져옴

    @Query("UPDATE User u SET u.accessToken = :accessToken WHERE u.id = :id")
    void updateAccessTokenByUsername(Long id, String accessToken);


}
