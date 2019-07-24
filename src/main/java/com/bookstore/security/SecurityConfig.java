package com.bookstore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String[] staticResources = {"/css/**", "/images/**"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(staticResources).permitAll()
                .antMatchers("/", "/register", "/register/payment").permitAll()
                .antMatchers("/books","/books/details", "/books/sort",
                        "/books/search", "/books/filter/{status}").permitAll()
                .antMatchers("/authors", "/authors/books", "/authors/search",
                        "/authors/books/search", "/authors/books/sort", "/authors/books/{status}").permitAll()
                .antMatchers("/publishers", "/publishers/books", "/publishers/search",
                        "/publishers/books/search", "/publishers/books/sort", "/publishers/books/{status}").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/loginform").permitAll()
                .loginProcessingUrl("/process_login")
                .usernameParameter("user")
                .passwordParameter("pass")
                .and().logout()
                .logoutUrl("/process_logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
    }
}
