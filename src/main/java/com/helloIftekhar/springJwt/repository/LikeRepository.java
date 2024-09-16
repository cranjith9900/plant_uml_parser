package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.ImagePost;
import com.helloIftekhar.springJwt.model.Like;
import com.helloIftekhar.springJwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByImagePostAndUser(ImagePost imagePost, User user);
    List<Like> findByImagePost(ImagePost imagePost);
    List<Like> findByUser(User user);
}
