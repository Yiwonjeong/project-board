package kr.co.crud.controller;

import kr.co.crud.domain.UserVO;
import kr.co.crud.entity.UserEntity;
import kr.co.crud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService service;


    @GetMapping("/user/login")
    public String loginForm() {
        return "user/login";
    }
    @PostMapping("/user/login")
    public String login(){
        return "redirect:/";
    }

    @GetMapping("/user/register")
    public String register() {
        return "/user/register";
    }

    /* User Register */
    @PostMapping("/user/register")
    public String insertUser(UserVO userVO){
        service.insertUser(userVO);
        return "redirect:/user/login";
    }




}
