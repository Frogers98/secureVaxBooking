package app.controller;

import app.exception.PostNotFoundException;
import app.exception.ReplyNotFoundException;
import app.exception.ReplyNotFoundExceptionPK;
import app.exception.UserNotFoundException;
import app.model.User;
import app.model.forum.Post;
import app.model.forum.Reply;
import app.repository.forum.PostRepository;
import app.repository.forum.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("forum")
public class ForumController {

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    PostRepository postRepository;

    // Return the forum landing page (lists all posts in the forum)
    @GetMapping
    public String showPosts(Model model) {
        List<Post> allPosts = postRepository.findAll();
        model.addAttribute("forumPosts", allPosts);
        return "list_forum_posts.html";

    }
    // Add a new post
    @PostMapping("/post")
    public void newPost(@Valid @RequestBody Post newPost) {
        System.out.println(newPost.getPost_title());
        System.out.println(newPost.getUser().getName());
        postRepository.save(newPost);
    }

    // Get all posts
    @GetMapping("/post")
    public @ResponseBody Iterable<Post> getPosts() {
        return postRepository.findAll();
    }

    // Get a particular post by id
    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable(value = "id") long id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    // Save a reply to the replies table and update the reply_id column in posts for the relevant post with the reply_id given
    @PostMapping("/reply")
    public void newReply(@Valid @RequestBody Reply reply) {
        System.out.println(reply.getReply_content());
        replyRepository.save(reply);
        postRepository.updateReplyId(reply.getReply_id(), reply.getPost().getPost_id());
    }

    // Get a certain reply by its post id
    @GetMapping("/reply/{post_id}")
        public Reply getReplyByPostId(@PathVariable(value = "post_id") long post_id) throws ReplyNotFoundException {
//            return replyRepository.findById(post_id).orElseThrow(() -> new ReplyNotFoundException(post_id));
       Reply reply = replyRepository.findByPostId(post_id);
       if (reply == null) {
           throw new ReplyNotFoundException(post_id);
       }
       else {
           return reply;
       }
        }

    // Get a certain reply by its reply_id
    @GetMapping("/reply/{reply_id}")
    public Reply getReplyByReplyId(@PathVariable(value = "reply_id") long reply_id) throws ReplyNotFoundExceptionPK {
        return replyRepository.findById(reply_id).orElseThrow(() -> new ReplyNotFoundExceptionPK(reply_id));
    }



    }