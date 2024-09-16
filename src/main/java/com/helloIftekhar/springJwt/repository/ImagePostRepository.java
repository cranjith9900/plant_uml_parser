package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.ImagePost;
import com.helloIftekhar.springJwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagePostRepository extends JpaRepository<ImagePost, Long> {
    List<ImagePost> findByUser(User user);
}

