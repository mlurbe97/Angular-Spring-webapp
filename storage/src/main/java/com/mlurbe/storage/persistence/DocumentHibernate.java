package com.mlurbe.storage.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlurbe.storage.models.Document;

@Repository
public interface DocumentHibernate extends JpaRepository<Document, Long> {

}
