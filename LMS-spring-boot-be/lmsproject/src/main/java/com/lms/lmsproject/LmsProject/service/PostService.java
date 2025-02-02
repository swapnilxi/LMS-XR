package com.lms.lmsproject.LmsProject.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.lmsproject.LmsProject.entity.Post;
import com.lms.lmsproject.LmsProject.entity.PostEnu;
import com.lms.lmsproject.LmsProject.entity.Teacher;
import com.lms.lmsproject.LmsProject.repository.PostRepo;

import com.lms.lmsproject.LmsProject.repository.TeacherRepo;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher getAuthenticatedTeacher() {

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();

        return teacherRepo.findByTeacherUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Teacher not found !"));
    }

    public List<Post> fetchAllPost() {
        return postRepo.findAll();
    }

    @Transactional
    public Post createPost(Post post) {
        // Validate title and content
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        // Create a new Post
        Post newPost = Post.builder()
                .postId(UUID.randomUUID().toString())
                .title(post.getTitle())
                .content(post.getContent())
                .teacher(getAuthenticatedTeacher()) // Associate the post with the logged-in teacher
                .teacherName(getAuthenticatedTeacher().getTeacherUsername())
                .catagories(PostEnu.REGULAR) // how can i take this from user ?
                .build();
        // Save the Post to the database
        return postRepo.save(newPost);
    }

    @Transactional
    public Post updatePost(Post post) {
        Teacher authenticatedUser = getAuthenticatedTeacher();
        Post existingPost = postRepo.findById(post.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!existingPost.getTeacher().getTeacherId().equals(authenticatedUser.getTeacherId())) {
            throw new IllegalArgumentException("You are not authorized to update this post");
        }
        if (post.getTitle() != null) {
            existingPost.setTitle(post.getTitle());
        }
        if (post.getContent() != null) {
            existingPost.setContent(post.getContent());
        }

        return postRepo.save(existingPost);
    }

    public Post findPostById(String id) {
        return postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void deletePost(String postId) {

        // Fetch the post by ID
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Check if the post belongs to the logged-in teacher
        if (!post.getTeacher().getTeacherId().equals(getAuthenticatedTeacher().getTeacherId())) {
            throw new RuntimeException("You are not authorized to delete this post");
        }

        // Delete the post if the logged-in teacher is the owner
        postRepo.delete(post);
    }
}
