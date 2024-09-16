package com.helloIftekhar.springJwt.service;

import com.helloIftekhar.springJwt.model.ImagePost;
import com.helloIftekhar.springJwt.model.Like;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.ImagePostRepository;
import com.helloIftekhar.springJwt.repository.LikeRepository;
import com.helloIftekhar.springJwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ImagePostRepository imagePostRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Method to allow a user to like an image post.
     *
     * @param userId      the ID of the user who is liking the post
     * @param imagePostId the ID of the image post to be liked
     * @return Like object representing the like that was saved
     */
    public Like likePost(Long userId, Long imagePostId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        ImagePost post = imagePostRepository.findById(imagePostId)
                .orElseThrow(() -> new RuntimeException("Image post not found"));

        // Check if the user has already liked the post
        if (likeRepository.findByImagePostAndUser(post, user).isPresent()) {
            throw new RuntimeException("User has already liked this post");
        }

        Like like = new Like();
        like.setUser(user);
        like.setImagePost(post);
        like.setCreatedAt(LocalDateTime.now());

        return likeRepository.save(like);
    }

    /**
     * Method to retrieve all likes for a given image post.
     *
     * @param postId the ID of the image post
     * @return List of Like objects representing all likes for the post
     */
    public List<Like> getLikesForPost(Long postId) {
        ImagePost post = imagePostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Image post not found"));
        return likeRepository.findByImagePost(post);
    }

    /**
     * Method to retrieve all likes made by a specific user.
     *
     * @param userId the ID of the user
     * @return List of Like objects representing all likes made by the user
     */
    public List<Like> getLikesByUser(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return likeRepository.findByUser(user);
    }
}

