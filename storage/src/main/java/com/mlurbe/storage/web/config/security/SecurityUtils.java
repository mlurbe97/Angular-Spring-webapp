package com.mlurbe.storage.web.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mlurbe.storage.models.User;
import com.mlurbe.storage.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityUtils {

    @Autowired
    private UserService userRepository;

    @Autowired
    public SecurityUtils(UserService userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoginUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null)
        {
            log.info("getLoginUser() authentication no es null.");
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            Optional<User> userRecup = userRepository.findByUsername(user.getUsername());
            if (userRecup.isPresent()) {
                return userRecup.get();
            } else {
                log.info("getLoginUser() authentication es null porque no se encuentra el usuario.");
                return null;
            }

        }
        log.info("getLoginUser() authentication si es null.");
        return null;
    }
    

}
