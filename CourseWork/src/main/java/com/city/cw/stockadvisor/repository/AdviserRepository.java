package com.city.cw.stockadvisor.repository;

import com.city.cw.stockadvisor.model.Adviser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdviserRepository extends JpaRepository<Adviser, Long> {
    Adviser findByEmail(String email);
}
