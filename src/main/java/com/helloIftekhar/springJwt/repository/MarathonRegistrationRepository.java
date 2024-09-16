package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.MarathonEvent;
import com.helloIftekhar.springJwt.model.MarathonRegistration;
import com.helloIftekhar.springJwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarathonRegistrationRepository extends JpaRepository<MarathonRegistration, Long> {
    Optional<MarathonRegistration> findByUserAndEvent(User user, MarathonEvent event);
}
