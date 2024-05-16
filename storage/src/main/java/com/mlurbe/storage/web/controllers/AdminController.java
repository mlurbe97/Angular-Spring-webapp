package com.mlurbe.storage.web.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlurbe.storage.models.User;
import com.mlurbe.storage.service.UserService;
import com.mlurbe.storage.web.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;

    }
    @RequestMapping("/me")
    public ResponseEntity<UserDTO> userAdmin (Principal user) {
        if (user == null) {
            log.info("WARN: getUser() No llega el usuario principal");
            return ResponseEntity.notFound()
                    .build();
        } else {
            log.info("service=userAdmin, user={}", user.getName());

            Optional<User> userOpt = userService.findByUsername(user.getName());
            User usuarioRecup = null;
            if (userOpt.isPresent()) {
                usuarioRecup = userOpt.get();
                UserDTO userSending = new UserDTO(usuarioRecup);
                return ResponseEntity.ok(userSending);
            } else {
                return ResponseEntity.notFound()
                        .build();
            }
        }
        
    }
}
