package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Integer> {
    Optional<ContactPerson> findByName(String name);
    Optional<ContactPerson> findByFirstname(String firstname);
    Optional<ContactPerson> findByPhoneNumberContact(String phoneNumber);
}
