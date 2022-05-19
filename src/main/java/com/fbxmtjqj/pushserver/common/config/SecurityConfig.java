package com.fbxmtjqj.pushserver.common.config;

import com.fbxmtjqj.pushserver.common.exception.handler.SecurityHandler;
import com.fbxmtjqj.pushserver.common.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] PUBLIC_URI = {
            // 유저 추가 및 로그인
            "/api/v1/addUser",
            "/api/v1/signIn",
            // Swagger
            "/api-docs.html",
            "/swagger-ui/**",
            "/v1/docs/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
                .headers()
                .frameOptions()
                .sameOrigin()
            .and()
                .exceptionHandling()
                    .accessDeniedHandler(new SecurityHandler())
                    .authenticationEntryPoint(new SecurityHandler())
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/users", "/api/v1/signIn").permitAll()
                .antMatchers(HttpMethod.GET, "/refresh/accessToken").permitAll()
                .anyRequest().authenticated()
            .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }
}
