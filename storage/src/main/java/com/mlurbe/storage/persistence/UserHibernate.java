package com.mlurbe.storage.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlurbe.storage.models.User;

@Repository
public interface UserHibernate extends JpaRepository<User, Long> {

    Optional<User> findByUsername (String username);

}
