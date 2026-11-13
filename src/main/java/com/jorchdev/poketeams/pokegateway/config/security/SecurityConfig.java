package com.jorchdev.poketeams.pokegateway.config.security;


import com.jorchdev.poketeams.pokegateway.client.frontend.CsrfCookieFilter;
import com.jorchdev.poketeams.pokegateway.client.pokechat.ChatSessionFilter;
import com.jorchdev.poketeams.pokegateway.client.pokeservice.PokeServiceSessionFilter;
import com.jorchdev.poketeams.pokegateway.client.security.SessionAuthenticationFilter;
import com.jorchdev.poketeams.pokegateway.components.InternalJwtIssuer;
import com.jorchdev.poketeams.pokegateway.components.session.SessionAuthenticator;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        config.setAllowedHeaders(
                List.of("*")
        );

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           SessionAuthenticator authenticator,
                                           TrainerRepository trainerRepository,
                                           InternalJwtIssuer issuer) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/callback", "/auth/me", "/graphql", "/graphiql", "/subscriptions", "/chat/**", "/chat-ws/**").permitAll()
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new SessionAuthenticationFilter(authenticator, trainerRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new ChatSessionFilter(authenticator, issuer), CsrfCookieFilter.class)
                .addFilterAfter(new PokeServiceSessionFilter(authenticator, issuer), ChatSessionFilter.class);

        return http.build();
    }
}
