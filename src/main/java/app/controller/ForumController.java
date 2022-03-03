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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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


//     Add a new post (not accessible by an endpoint, just used by ForumConfig for db population
    public void newPost(@Valid @RequestBody Post newPost) {
        postRepository.save(newPost);
    }

    // Add a new post from a form request. Couldn't get it to accept a Post object so instead it accepts all the required
    // parameters for a post, I grab the relevant user from the db and make a Post and save it with that user.
    @PostMapping("/post")
    public String newPostForm(@RequestParam("user_id") Long user_id, @RequestParam("post_title") String post_title,
                              @RequestParam("post_content") String post_content) {

        System.out.println("submission received from user " + user_id + ": " + post_title);
        User user = userRepository.findByID(user_id);
        Post newPost = new Post(user, post_title, post_content);
        postRepository.save(newPost);
        return "redirect:/forum";
    }


    // Get all posts, not currently used but added endpoint in case we need it. Can remove if need be for security
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
            // Add the post id for this post to the model so we can access it
        }

        return "show_post.html";
    }

    // Save a reply to the replies table and update the reply_id column in posts for the relevant post with the reply_id given
   // (not accessible by an endpoint, just used by ForumConfig for db population)
    public void newReply(@Valid @RequestBody Reply reply) {
        replyRepository.save(reply);
        postRepository.updateReplyId(reply.getReply_id(), reply.getPost().getPost_id());
    }

    // Add a new post from a form request. Couldn't get it to accept a Reply object so instead it accepts all the required
    // parameters for a reply, I grab the relevant post from the db and make a Reply and save it with that Post.
    @PostMapping("/reply")
    public String newReplyForm(@RequestParam("post_id") Long post_id, @RequestParam("reply_content") String reply_content) {

        System.out.println("reply received for post " + post_id + ": " + reply_content);
        Post post = postRepository.getById(post_id);
        Reply newReply = new Reply(post, reply_content);
        replyRepository.save(newReply);
        postRepository.updateReplyId(newReply.getReply_id(), newReply.getPost().getPost_id());
        return "redirect:/forum";
    }

    // Get a certain reply by its post id (not currently in use but endpoint is here if we need it,
    // can remove endpoint for security reasons if we deem it necessary)
//    @GetMapping("/reply/{post_id}")
//        public Reply getReplyByPostId(@PathVariable(value = "post_id") long post_id) throws ReplyNotFoundException {
//       Optional<Reply> replyOptional = replyRepository.findByPostId(post_id);
//       if (replyOptional.isPresent()) {
//           Reply reply = replyOptional.get();
//           return reply;
//       }
//       else {
//           throw new ReplyNotFoundException(post_id);
//       }
//        }

    // Get a certain reply by its reply_id (not currently in use but endpoint is here if we need it,
    // can remove endpoint for security reasons if we deem it necessary)
//    @GetMapping("/reply/{reply_id}")
//    public Reply getReplyByReplyId(@PathVariable(value = "reply_id") long reply_id) throws ReplyNotFoundExceptionPK {
//        return replyRepository.findById(reply_id).orElseThrow(() -> new ReplyNotFoundExceptionPK(reply_id));
//    }

    }