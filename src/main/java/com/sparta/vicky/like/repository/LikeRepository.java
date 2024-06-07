package com.sparta.vicky.like.repository;

import com.sparta.vicky.like.entity.ContentType;
import com.sparta.vicky.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByContentTypeAndContentId(ContentType contentType, Long contentId);

}
