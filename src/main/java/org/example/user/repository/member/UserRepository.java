package org.example.repository.member;

import org.example.user.domain.entity.member.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // username 사용자 정보를 가져옴
}
