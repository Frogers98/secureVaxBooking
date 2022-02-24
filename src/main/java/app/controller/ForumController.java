package app.controller;

import app.exception.PostNotFoundException;
import app.exception.UserNotFoundException;
import app.model.User;
import app.model.forum.Post;
import app.repository.forum.PostRepository;
import app.repository.forum.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("forum")
public class ForumController {

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    PostRepository postRepository;

    // Add a new post
    @PostMapping
    public void newPost(@Valid @RequestBody Post newPost) {
        System.out.println(newPost.getPost_title());
        System.out.println(newPost.getUser().getName());
        postRepository.save(newPost);
    }

    // Get all posts
    @GetMapping
    public @ResponseBody Iterable<Post> getPosts() {
        return postRepository.findAll();
    }

    // Get a particular post by id
    @GetMapping("{id}")
    public  Post getPost(@PathVariable(value = "id") long id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }


}
