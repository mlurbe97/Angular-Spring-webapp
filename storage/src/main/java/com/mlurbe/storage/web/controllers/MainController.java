package com.mlurbe.storage.web.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mlurbe.storage.models.User;
import com.mlurbe.storage.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class MainController {

    @Autowired
    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/home")
    // @PreAuthorize("")
    public String homePage () {
        return "Storage Documents APP";
    }

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public User createUser (@RequestBody User user) {
        log.info("process=create-user, user_email={}", user.getEmail());
        log.info("process=create-user, user_username={}", user.getUsername());
        log.info("process=create-user, user_password={}", user.getPassword());
        log.info("process=create-user, user_role={}", user.getRole());
        return userService.createUser(user);
    }

}
