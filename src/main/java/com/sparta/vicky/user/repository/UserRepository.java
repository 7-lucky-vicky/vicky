package com.sparta.vicky.user.repository;

import com.sparta.vicky.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
