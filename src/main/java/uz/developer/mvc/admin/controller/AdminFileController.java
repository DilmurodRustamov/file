package uz.developer.mvc.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/file")
public class AdminFileController {

    @GetMapping("/list")
    public String getProduct(){
        return "admin";
    }
}
