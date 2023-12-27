package com.city.cw.stockadvisor.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.city.cw.stockadvisor.service.AdviserDetailsService;



@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
	private OAuth2SuccessHandler oauthHandler;
	
	private final AdviserDetailsService adviserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(AdviserDetailsService adviserDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.adviserDetailsService = adviserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
	
	 @Bean
	    public AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        provider.setUserDetailsService(adviserDetailsService);
	        provider.setPasswordEncoder(passwordEncoder);
	        return provider;
	    }
	 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                   .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/*login**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/adviser-reg","/adviserReg").permitAll()
                        .requestMatchers("/adviserHome").permitAll()
                        .requestMatchers("/user-home").authenticated()
                        .requestMatchers("/adviser-home").authenticated()
                        .anyRequest().authenticated();
                })
                           
                .formLogin(form->form
                		.loginPage("/adviser-login")
                		.loginProcessingUrl("/adviserHome")
                		.defaultSuccessUrl("/adviser-home")
                		.permitAll()
                )
                .oauth2Login(login -> {
                    System.out.println("Login success");
                    login.successHandler(oauthHandler);

                })

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/").permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true));
        
				return http.build();
	}


	
}
