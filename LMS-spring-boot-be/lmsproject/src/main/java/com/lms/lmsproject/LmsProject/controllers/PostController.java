package com.lms.lmsproject.LmsProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.lmsproject.LmsProject.customApiResponse.APIResponse;
import com.lms.lmsproject.LmsProject.entity.Post;
import com.lms.lmsproject.LmsProject.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/all-post")
    public ResponseEntity<APIResponse<List<Post>>> getAllPosts() {
        try {
            List<Post> posts = postService.fetchAllPost();
            return new ResponseEntity<>(
                    new APIResponse<List<Post>>(HttpStatus.OK.value(), "Success", posts),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<List<Post>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.OK);
        }
    }

    @PostMapping("/create-post")
    public ResponseEntity<APIResponse<Post>> createPost(@RequestBody Post post) {
        try {
            Post createdPost = postService.createPost(post);
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.CREATED.value(), "Post created successfully", createdPost),
                    HttpStatus.CREATED);
        } catch (Exception IllegalArgumentException) {
            return new ResponseEntity<>(
                    new APIResponse<Post>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            IllegalArgumentException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/find-post/{id}")
    public ResponseEntity<APIResponse<Post>> findPostById(@PathVariable String id) {
        try {
            Post post = postService.findPostById(id);
            return new ResponseEntity<>(
                    new APIResponse<Post>(HttpStatus.OK.value(), "Success", post),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<Post>(HttpStatus.NOT_FOUND.value(), "Error", null),
                    HttpStatus.OK);
        }
    }

    @PutMapping(path = "/update-post")
    public ResponseEntity<APIResponse<Post>> updatePost(@RequestBody Post post) {
        try {
            Post updatedPost = postService.updatePost(post);
            return new ResponseEntity<>(new APIResponse<Post>(HttpStatus.OK.value(), "Success", updatedPost),
                    HttpStatus.OK);
        } catch (Exception IllegalArgumentException) {
            return new ResponseEntity<>(new APIResponse<Post>(HttpStatus.CONFLICT.value(), "Error", null),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<APIResponse<Void>> deletePost(@PathVariable String postId) {
        try {

            postService.deletePost(postId);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Post deleted successfully", null),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Post Id Not Found !", null),
                    HttpStatus.OK);

        }
    }
}
