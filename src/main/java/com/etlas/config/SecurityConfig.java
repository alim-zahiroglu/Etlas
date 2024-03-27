package com.etlas.config;

import com.etlas.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityService securityService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChainForUI(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login","/user/reset-password", "/assets/**",
                                "/data-table-assets/**", "/images/**").permitAll()
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login") // Specifies the URL of your custom login page
//                            .loginProcessingUrl("/authentication") // The URL to which the login form should be submitted for authentication
//                            .usernameParameter("username") // The parameter name in the login form for the username field
//                            .passwordParameter("password") // The parameter name in the login form for the password field
                        .defaultSuccessUrl("/card/list", true))
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                )
                .rememberMe(token->token.tokenValiditySeconds(300).key("Etlas").userDetailsService(securityService)); // Redirects the user to "/home" after successful login

        return http.build();
    }
    @Bean
    ApplicationListener<AuthenticationSuccessEvent> successEvent(){
        return event ->
                log.info("success: {}", event.getAuthentication());
    }

}