package com.mlurbe.storage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mlurbe.storage.models.User;
import com.mlurbe.storage.persistence.UserHibernate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final UserHibernate userRepository;

    @Autowired
    public UserService(UserHibernate userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById (Long id) {
        log.info("service=getUserById");
        return userRepository.findById(id);
    }

    public List<User> getAllUsers () {
        log.info("service=getAllUsers");
        return userRepository.findAll();
    }

    @Transactional
    public User createUser (User user) {
        log.info("service=createUser");
        log.info(user.getRole());
        log.info(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser (User user) {
        log.info("service=updateUser");
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser (Long userId) {
        log.info("service=deleteUser");
        userRepository.deleteById(userId);
    }

    public Optional<User> findByUsername (String username) {
        log.info("service=findByUsername->" + username);
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void save (User user) {
        log.info("service=save");
        userRepository.save(user);
    }
}
