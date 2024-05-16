package com.mlurbe.storage.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlurbe.storage.models.Content;

@Repository
public interface ContentHibernate extends JpaRepository<Content, Long> {

}
