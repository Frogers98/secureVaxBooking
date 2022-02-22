package app.controller;

import app.repository.PostRepository;
import app.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("forum")
public class ForumController {

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    PostRepository postRepository;


    @GetMapping
    @ResponseBody
    public String test() {
        return "test";
    }


}
