package me.igrade.gateway.security.config;

import lombok.RequiredArgsConstructor;
import me.igrade.gateway.security.handlers.CustomAccessDeniedHandler;
import me.igrade.gateway.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;



@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {



    private final JwtAuthenticationFilter authenticationFilter;

    private final JwtDecoder jwtDecoder;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{

        security.csrf(AbstractHttpConfigurer::disable);
        security.oauth2ResourceServer(Customizer.withDefaults());
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        security.oauth2ResourceServer(o2auth -> o2auth.jwt(jwtConfigurer -> {
            jwtConfigurer.decoder(jwtDecoder);
            jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter());
        }));
        //Authenticate endpoint
        security.authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(GET,"/eureka/**").permitAll()
                                .requestMatchers(POST, "/api/v1/grade/create").hasAuthority("TEACHER_WRITE")
                                .requestMatchers(POST,"/api/v1/note/create").hasAuthority("TEACHER_WRITE")
                                .requestMatchers(GET,"/api/v1/class/all").hasAuthority("ADMIN_READ")
                                .requestMatchers(GET,"/api/v1/subject/class/**").hasAuthority("TEACHER_READ")
                                .requestMatchers(POST,"/api/v1/class/create").hasAuthority("TEACHER_WRITE")
                                .requestMatchers(POST,"/api/v1/subject/create").hasAuthority("TEACHER_WRITE")
                                .requestMatchers(GET,"/api/v1/class/code/**").hasAuthority("TEACHER_READ")
                                .requestMatchers(DELETE, "/api/v1/grade/delete/**").hasAuthority("TEACHER_DELETE")
                                .requestMatchers(POST,"/api/v1/subject/create").hasAuthority("TEACHER_WRITE")
                                .requestMatchers(POST,"/api/v1/note/update/**").hasAuthority("TEACHER_WRITE")
                                .requestMatchers(DELETE,"/api/v1/subject/delete/**").hasAuthority("TEACHER_DELETE")
                                .requestMatchers(DELETE,"/api/v1/class/delete/**").hasAuthority("TEACHER_DELETE")
                                .requestMatchers(POST,"/api/v1/class/update/**").hasAuthority("TEACHER_WRITE")
                                .requestMatchers(DELETE,"/api/v1/note/delete/**").hasAuthority("TEACHER_DELETE")
                                .requestMatchers(POST,"/api/v1/class/create").hasAuthority("ADMIN_WRITE")
                                .requestMatchers(POST, "/api/v1/user/login").permitAll()
                                .requestMatchers(GET, "/api/v1/user/student/register").permitAll()
                                .requestMatchers(GET, "/api/v1/user/me").hasAuthority("USER")
                                .requestMatchers(POST, "/api/v1/user/changePassword").hasAuthority("USER")
                                .requestMatchers(GET, "/api/v1/user/notifications").hasAuthority("USER")
                                .requestMatchers(POST, "/api/v1/user/notifications/checked/**").hasAuthority("USER")
                                .requestMatchers(GET,"/api/v1/user/email/**").hasAuthority("ADMIN_READ")
                                .requestMatchers(GET, "/api/v1/user/id/**").hasAuthority("ADMIN_READ")
                                .requestMatchers(GET, "/api/v1/user/student/grades").hasAuthority("STUDENT_READ")
                                .requestMatchers(GET, "/api/v1/user/student/class/**").hasAuthority("STUDENT_READ")
                                .requestMatchers(GET, "/api/v1/user/student/grades").hasAuthority("STUDENT_READ")
                                .requestMatchers(GET, "/api/v1/user/student/notes-stat").hasAuthority("STUDENT_READ")
                                .requestMatchers(GET, "/api/v1/user/student/notes").hasAuthority("STUDENT_READ")
                                .requestMatchers(GET, "/api/v1/user/student/class").hasAuthority("STUDENT_READ")
                                .requestMatchers(GET, "/api/v1/user/teacher/grades").hasAuthority("TEACHER_READ")
                                .requestMatchers(GET, "/api/v1/user/teacher/notes").hasAuthority("TEACHER_READ")
                                .requestMatchers(GET, "/api/v1/user/teacher/subjects").hasAuthority("TEACHER_READ")
                                .requestMatchers(GET, "/api/v1/user/teacher/classes").hasAuthority("TEACHER_READ")



                )
                .exceptionHandling(exception ->{
                    exception.accessDeniedHandler(customAccessDeniedHandler);
                })
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return security.build();
    }
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities"); // defaults to "scope" or "scp"
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // defaults to "SCOPE_"

        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200")); // angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*")); // Akceptuj wszystkie nagłówki
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
