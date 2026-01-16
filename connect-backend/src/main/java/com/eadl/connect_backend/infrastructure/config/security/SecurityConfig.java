package com.eadl.connect_backend.infrastructure.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/actuator/**",
                                "/api/categories/**",
                                "/api/storage/**",
                                "/uploads/**")
                        .permitAll()
                        // Technician-only endpoints
                        .requestMatchers("/api/technician-skills/**").hasRole("TECHNICIAN")
                        // Admin-only endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Tout le reste nécessite une authentification
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public static class JwtAuthFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;
        private final UserDetailsService userDetailsService;

        public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
            this.jwtUtil = jwtUtil;
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void doFilterInternal(
                @NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain) throws ServletException, IOException {

            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);

                var userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isTokenValid(token, userDetails)) {
                    // On ajoute le ROLE_ devant le rôle pour que Spring Security comprenne
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDetails.getRole().name()));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}