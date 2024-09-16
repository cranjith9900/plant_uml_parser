package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.model.Friendship;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.FriendshipRepository;
import com.helloIftekhar.springJwt.repository.UserRepository;
import com.helloIftekhar.springJwt.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        friendshipService.addFriend(userId, friendId);
        return ResponseEntity.ok("Friend added successfully!");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Friendship>> getFriends(@PathVariable Long userId) {
        List<Friendship> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }
}

