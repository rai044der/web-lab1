package ru.abradox.carsbusinesscard.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(spec -> spec.configurationSource(corsConfigurationSource))
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManager -> sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/actuator/**")).authenticated()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/internal/**")).authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(customizer -> customizer
                        .loginPage("/api/auth")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth.html")
                        .permitAll()
                )
                .logout(customizer -> customizer
                        .logoutSuccessUrl("/")
                        .permitAll());
        return http.build();
    }


}