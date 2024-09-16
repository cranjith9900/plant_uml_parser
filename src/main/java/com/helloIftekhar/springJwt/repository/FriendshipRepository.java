package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.Friendship;
import com.helloIftekhar.springJwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser(User user);
    List<Friendship> findByFriend(User user);
}
