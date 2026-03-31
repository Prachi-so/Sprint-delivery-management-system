package com.example.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {


	 @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	        http
	            .csrf(csrf -> csrf.disable())

	            .sessionManagement(session ->
	                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

	            .authorizeHttpRequests(auth -> auth
	                //.requestMatchers("/tracking/events/**").permitAll()
	            		.requestMatchers(
	                            "/v3/api-docs/**",
	                            "/swagger-ui/**",
	                            "/swagger-ui.html"
	                        ).permitAll()
	                .anyRequest().authenticated()
	            )

	            // 🔥 IMPORTANT: add header-based auth filter
	            .addFilterBefore(requestHeaderAuthenticationFilter(),
	                    org.springframework.security.web.authentication.AnonymousAuthenticationFilter.class);

	    
	        return http.build();
	    }

	    // 🔹 2. Header Authentication Filter (reads from Gateway)
	    @Bean
	    public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() {

	        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();

	        filter.setPrincipalRequestHeader("X-User-Id");  // from Gateway
	        filter.setCredentialsRequestHeader("X-Role");   // role from Gateway
	        filter.setExceptionIfHeaderMissing(false);

	        // 🔥 attach authentication manager here
	        filter.setAuthenticationManager(authenticationManager());

	        return filter;
	    }

	    // 🔹 3. Authentication Manager (THIS IS YOUR QUESTION)
	    @Bean
	    public AuthenticationManager authenticationManager() {

	        return authentication -> {

	            String role = (String) authentication.getCredentials();

	            if (role != null) {

	                role = role.toUpperCase();

	                if (!role.startsWith("ROLE_")) {
	                    role = "ROLE_" + role;
	                }

	                List<GrantedAuthority> authorities =
	                        List.of(new SimpleGrantedAuthority(role));

	                return new UsernamePasswordAuthenticationToken(
	                        authentication.getPrincipal(),
	                        authentication.getCredentials(),
	                        authorities
	                );
	            }

	            return authentication;
	        };
	    }
	    @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**")
	                    .allowedOriginPatterns("*")
	                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
	                    .allowedHeaders("*")
	                    .allowCredentials(false);
	            }
	        };
	    }
	    @Bean
		 public OpenAPI customOpenAPI() {
		        return new OpenAPI()
		                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
		                .components(new Components()
		                        .addSecuritySchemes("bearerAuth",
		                                new SecurityScheme()
		                                        .name("Authorization")
		                                        .type(SecurityScheme.Type.HTTP)
		                                        .scheme("bearer")
		                                        .bearerFormat("JWT")
		                        ))
		                        .servers(List.of(
		                                new Server().url("http://localhost:8888/tracking-service"))
		                        		);
		                        
		                
		    }
	    
	  
}
