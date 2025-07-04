package com.app85soft.qiqishop.repositories.contact;

import com.app85soft.qiqishop.entities.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>, ContactRepositoryCustom {
}
