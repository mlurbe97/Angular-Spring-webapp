package com.mlurbe.storage.web.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlurbe.storage.models.util.AuthenticationRequest;
import com.mlurbe.storage.models.util.UserTokenState;
import com.mlurbe.storage.web.config.security.TokenHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenHelper tokenHelper) {
        this.tokenHelper = tokenHelper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken (
            @RequestBody AuthenticationRequest authenticationRequest)
        throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        log.info("UserDetails username -> " + user.getUsername());
        log.info("UserDetails password -> " + user.getPassword());
        String jws = tokenHelper.generateToken(user.getUsername());
        long expiresIn = tokenHelper.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(jws, expiresIn));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refreshAuthenticationToken (HttpServletRequest request, Principal principal) {

        String authToken = tokenHelper.getToken(request);

        if (authToken != null && principal != null) {
            String refreshedToken = tokenHelper.refreshToken(authToken);
            long expiresIn = tokenHelper.getExpiredIn();

            return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return ResponseEntity.accepted()
                    .body(userTokenState);
        }
    }
}