package app.controller;

import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DefaultController {

    @Autowired
    private UserService service;

    @GetMapping("")
    public String basePage(Model model) {
        List<String> nationalityList= service.getAllUsers().stream().map(x->x.getNationality()).collect(Collectors.toList());
        List<String> sex= service.getAllUsers().stream().map(x->x.getSex()).collect(Collectors.toList());
        int count = nationalityList.size();
        model.addAttribute("nation", nationalityList);
        model.addAttribute("sex", sex);
        return "index";
    }
}
