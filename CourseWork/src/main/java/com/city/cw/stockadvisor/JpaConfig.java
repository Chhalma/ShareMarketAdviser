package com.city.cw.stockadvisor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.city.cw.stockadvisor.repository")
public class JpaConfig {
    // Configuration if needed
}