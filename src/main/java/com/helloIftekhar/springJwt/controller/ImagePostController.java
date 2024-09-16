package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.model.ImagePost;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.ImagePostRepository;
import com.helloIftekhar.springJwt.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImagePostController {

    private final ImagePostRepository imagePostRepository;
    private final UserRepository userRepository;

    public ImagePostController(ImagePostRepository imagePostRepository, UserRepository userRepository) {
        this.imagePostRepository = imagePostRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("description") String description,
                                              @RequestParam("image") MultipartFile file,
                                              @AuthenticationPrincipal User user) {
        // Store the image locally and get the file path
        String imageUrl = storeImage(file);

        // Create a new ImagePost entity
        ImagePost post = new ImagePost();
        post.setUser(user);
        post.setDescription(description);
        post.setImageUrl(imageUrl);  // Store the local file path
        post.setCreatedAt(LocalDateTime.now());

        // Save the image post details in the database
        imagePostRepository.save(post);

        return ResponseEntity.ok("Image uploaded and path saved successfully!");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ImagePost>> getUserImages(@PathVariable Integer userId) {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("User found: " + user.getUsername());  // Log user info

        // Fetch and return the user's image posts
        List<ImagePost> posts = imagePostRepository.findByUser(user);

        System.out.println("Number of posts found: " + posts.size());  // Log number of posts

        return ResponseEntity.ok(posts);
    }


    // Add the storeImage method here to save images locally
    private String storeImage(MultipartFile file) {
        try {
            // Get the original filename
            String fileName = file.getOriginalFilename();

            // Define the path where the image will be stored
            String uploadDir = "uploads/";

            // Create the directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file locally
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the path where the image is stored (e.g., "uploads/filename.jpg")
            return filePath.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image: " + e.getMessage());
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Load the image as a resource
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)  // Adjust media type based on the image format
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
