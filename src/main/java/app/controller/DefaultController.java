package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
    @GetMapping("")
    public String basePage() {
        return "index";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
