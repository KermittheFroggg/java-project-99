package hexlet.code.config;

import hexlet.code.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;


//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private JwtDecoder jwtDecoder;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserService userService;
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
//            throws Exception {
//        return http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/login").permitAll()
//                        .requestMatchers("/api/pages/*").permitAll()
//                        .requestMatchers("/api/pages").permitAll()
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/index.html").permitAll()
//                        .requestMatchers("/assets/**").permitAll()
//                        .anyRequest().authenticated())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2ResourceServer((rs) -> rs.jwt((jwt) -> jwt.decoder(jwtDecoder)))
//                .httpBasic(withDefaults())
//                .build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .build();
//    }
//
//    @Bean
//    public AuthenticationProvider daoAuthProvider(AuthenticationManagerBuilder auth) {
//        var provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            HandlerMappingIntrospector introspector,
            JwtDecoder jwtDecoder
    )
            throws Exception {

        var mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/login")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/index.html")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/assets/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui.html")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui/**")).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(rs -> rs.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @SneakyThrows
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public AuthenticationProvider daoAuthProvider(
            PasswordEncoder passwordEncoder,
            UserService userService
    ) {
        var provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}