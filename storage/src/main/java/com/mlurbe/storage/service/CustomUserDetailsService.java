package com.mlurbe.storage.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mlurbe.storage.models.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void changePassword (String oldPassword, String newPassword) {

        Authentication currentUser = SecurityContextHolder.getContext()
                .getAuthentication();
        String username = currentUser.getName();

        log.debug("Re-authenticating user '" + username + "' for password change request.");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));

        log.debug("Changing password for user '" + username + "'");

        User user = (User) loadUserByUsername(username);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);

    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        log.info("CustomUserDetailsService->" + username);
        Optional<User> user = userService.findByUsername(username);
        User usuarioRecup = null;
        if (user.isPresent()) {
            usuarioRecup = user.get();
            log.info("FOUD user with username -> " + usuarioRecup.getUsername());
            log.info("Encoded Password -> " + usuarioRecup.getPassword());
        } else {
            log.info("NOT FOUD->" + username);
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        if (usuarioRecup.getEnabled() == User.NOT_ENABLED) {
            Object[] parameters = { username };
            String mensaje = messageSource.getMessage("auth.user_suspended", parameters,
                    LocaleContextHolder.getLocale());
            throw new UsernameNotFoundException(mensaje);
        }

        String role = "ROLE_";
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (usuarioRecup.getRole()
                .equals(User.ROLE_ADMIN)) {
            role += User.ROLE_ADMIN;
        } else {
            role += User.ROLE_USER;
        }
        authorities.add(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(username, usuarioRecup.getPassword(), authorities);

    }

    public User findUserSessionByUsername (Object principal) {
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        if (username == null) {
            log.info("findUserSessionByUsername() Session not found!");
            return null;
        }

        Optional<User> user = userService.findByUsername(username);
        User usuarioRecup = null;
        if (user.isPresent()) {
            usuarioRecup = user.get();
            log.info("FOUD user with username -> " + usuarioRecup.getUsername());
            log.info("Encoded Password -> " + usuarioRecup.getPassword());
        } else {
            log.info("NOT FOUD->" + username);
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        return usuarioRecup;
    }
}
