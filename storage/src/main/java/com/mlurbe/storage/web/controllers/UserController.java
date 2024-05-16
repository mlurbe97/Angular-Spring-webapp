package com.mlurbe.storage.web.controllers;


import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlurbe.storage.models.User;
import com.mlurbe.storage.models.util.ChangePassword;
import com.mlurbe.storage.service.CustomUserDetailsService;
import com.mlurbe.storage.service.UserService;
import com.mlurbe.storage.web.config.security.TokenHelper;
import com.mlurbe.storage.web.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService, CustomUserDetailsService userDetailsService, TokenHelper tokenHelper) {
        this.userService = userService;
        this.tokenHelper = tokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllFiles () {
        List<User> users = userService.getAllUsers();
        List<UserDTO> responseClasses = users.stream()
                .map(user -> {
                    return new UserDTO(user);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(responseClasses);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser (HttpServletRequest request, Principal user) {
        log.info(request.toString());
        // log.info(user.toString());
        String authToken = tokenHelper.getToken(request);
        log.info("AUTH-TOKEN=" + authToken);
        String username = tokenHelper.getUsernameFromToken(authToken);

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User usuarioRecup = userOpt.get();
            UserDTO userSending = new UserDTO(usuarioRecup);
            return ResponseEntity.ok(userSending);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @RequestMapping("/mod")
    public ResponseEntity<UserDTO> getUserMod (Principal user) {
        if (user == null) {
            log.info("WARN: getUser() No llega el usuario principal");
            return ResponseEntity.notFound()
                    .build();
        } else {
            Optional<User> userOpt = userService.findByUsername(user.getName());
            if (userOpt.isPresent()) {
                User usuarioRecup = userOpt.get();
                UserDTO userSending = new UserDTO(usuarioRecup);
                return ResponseEntity.ok(userSending);
            } else {
                return ResponseEntity.notFound()
                        .build();
            }
        }
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<?> changePassword (@RequestBody ChangePassword changePassword) {
        userDetailsService.changePassword(changePassword.getOldPassword(), changePassword.getNewPassword());
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.accepted()
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById (@PathVariable Long id) {
        log.info("process=get-user, user_id={}", id);
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User usuarioRecup = userOpt.get();
            UserDTO userSending = new UserDTO(usuarioRecup);
            return ResponseEntity.ok(userSending);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser (@PathVariable Long id, @RequestBody UserDTO user) {
        log.info("process=update-user, user_id={}", id);
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User usuarioRecup = userOpt.get();
            usuarioRecup.setEmail(user.getEmail());
            usuarioRecup.setRole(user.getRole());
            usuarioRecup.setUsername(user.getUsername());
            usuarioRecup.setEnabled(user.getEnabled());
            userService.updateUser(usuarioRecup);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser (@PathVariable Long id) {
        log.info("process=delete-user, user_id={}", id);
        userService.deleteUser(id);
    }

}
