package com.sparta.vicky.like.repository;

import com.sparta.vicky.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
