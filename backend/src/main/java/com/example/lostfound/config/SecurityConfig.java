package com.example.lostfound.config;
import com.example.lostfound.security.JwtFilter; import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.http.HttpMethod; import org.springframework.security.config.Customizer; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; import org.springframework.web.cors.*; import java.util.Arrays;
@Configuration public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain filterChain(HttpSecurity h,JwtFilter f)throws Exception{
  h.csrf(c->c.disable()).cors(Customizer.withDefaults()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
  .authorizeHttpRequests(a->a.requestMatchers("/api/health","/api/auth/**").permitAll().requestMatchers(HttpMethod.GET,"/api/items/**").permitAll().anyRequest().authenticated()).addFilterBefore(f,UsernamePasswordAuthenticationFilter.class);return h.build();
 }
 @Bean CorsConfigurationSource cors(@Value("${app.cors.allowed-origins}") String origins){
  CorsConfiguration c=new CorsConfiguration();c.setAllowedOrigins(Arrays.stream(origins.split(",")).map(String::trim).toList());c.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));c.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
  UrlBasedCorsConfigurationSource s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/**",c);return s;
 }
}
