package com.helloIftekhar.springJwt.service;

import com.helloIftekhar.springJwt.model.Friendship;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.FriendshipRepository;
import com.helloIftekhar.springJwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public Friendship addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(Math.toIntExact(friendId))
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setCreatedAt(LocalDateTime.now());

        return friendshipRepository.save(friendship);
    }

    public List<Friendship> getFriends(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return friendshipRepository.findByUser(user);
    }
}
