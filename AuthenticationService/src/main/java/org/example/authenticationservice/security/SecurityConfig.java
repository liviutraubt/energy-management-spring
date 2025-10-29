package org.example.authenticationservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //oricine
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        //ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/auth/getall").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/**").hasRole("ADMIN")

                        // orice altceva protejat
                        .anyRequest().denyAll()
                )
                .addFilterBefore(new JwtFilter(jwtService), BasicAuthenticationFilter.class)
                .build();
    }

    static class JwtFilter extends BasicAuthenticationFilter {
        private final JwtService jwt;
        public JwtFilter(JwtService jwt) { super(authentication -> authentication); this.jwt = jwt; }

        @Override
        protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
                throws IOException, ServletException {
            var auth = req.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                var token = auth.substring(7);
                try {
                    var user = jwt.validateAndGetSubject(token);
                    var role = jwt.getRole(token); // "USER" sau "ADMIN"
                    var authentication = new UsernamePasswordAuthenticationToken(
                            user, null,
                            role == null ? List.of() : List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception ignored) { /* token invalid -> rămâne neautentificat */ }
            }
            chain.doFilter(req, res);
        }
    }
}
