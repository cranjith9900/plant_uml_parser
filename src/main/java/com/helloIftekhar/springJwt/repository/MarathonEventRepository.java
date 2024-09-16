package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.MarathonEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarathonEventRepository extends JpaRepository<MarathonEvent, Long> {
}
