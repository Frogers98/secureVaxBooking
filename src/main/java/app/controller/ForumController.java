package app.controller;

import app.exception.PostNotFoundException;
import app.exception.ReplyNotFoundException;
import app.exception.ReplyNotFoundExceptionPK;
import app.model.User;
import app.model.forum.Post;
import app.model.forum.Reply;
import app.repository.UserRepository;
import app.repository.forum.PostRepository;
import app.repository.forum.ReplyRepository;
import app.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("forum")
public class ForumController {

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    // Return the forum landing page (lists all posts in the forum)
    @GetMapping
    public String showPosts(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<Post> allPosts = postRepository.findAll();
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);
        model.addAttribute("forumPosts", allPosts);
        model.addAttribute("user", user);
        System.out.println("logged in user is:" + user.getUser_id());
        return "list_forum_posts.html";

    }
    // Add a new post
    @PostMapping("/post")
    public void newPost(@Valid @RequestBody Post newPost) {
        postRepository.save(newPost);
    }

    // Get all posts
    @GetMapping("/post")
    public @ResponseBody Iterable<Post> getPosts() {
        return postRepository.findAll();
    }

    // Get a particular post by id and return show_post.html. Add the post (and reply) to the model if present
    @GetMapping("/post/{id}")
    public String getPost(@PathVariable(value = "id") long id, Model model) throws PostNotFoundException {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            model.addAttribute("post", post);
        }
        else {
            throw new PostNotFoundException(id);
        }

//       No exception thrown if a reply isn't found as we still want to show the post as normal, just not the reply
        Optional<Reply> replyOptional = replyRepository.findByPostId(id);
        if (replyOptional.isPresent()) {
            Reply reply = replyOptional.get();
            System.out.println("Reply id for post " + id + " is " + reply.getReply_id());
            model.addAttribute("reply", reply);
        }

        return "show_post.html";
    }

    // Save a reply to the replies table and update the reply_id column in posts for the relevant post with the reply_id given
    @PostMapping("/reply")
    public void newReply(@Valid @RequestBody Reply reply) {
        replyRepository.save(reply);
        postRepository.updateReplyId(reply.getReply_id(), reply.getPost().getPost_id());
    }

    // Get a certain reply by its post id (not currently in use but endpoint is here if we need it,
    // can remove endpoint for security reasons if we deem it necessary)
    @GetMapping("/reply/{post_id}")
        public Reply getReplyByPostId(@PathVariable(value = "post_id") long post_id) throws ReplyNotFoundException {
       Optional<Reply> replyOptional = replyRepository.findByPostId(post_id);
       if (replyOptional.isPresent()) {
           Reply reply = replyOptional.get();
           return reply;
       }
       else {
           throw new ReplyNotFoundException(post_id);
       }
        }

    // Get a certain reply by its reply_id (not currently in use but endpoint is here if we need it,
    // can remove endpoint for security reasons if we deem it necessary)
    @GetMapping("/reply/{reply_id}")
    public Reply getReplyByReplyId(@PathVariable(value = "reply_id") long reply_id) throws ReplyNotFoundExceptionPK {
        return replyRepository.findById(reply_id).orElseThrow(() -> new ReplyNotFoundExceptionPK(reply_id));
    }

    }