package com.example.demo.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


import reactor.core.publisher.Mono;




@Component
public class JwtFilterAuthentication implements GlobalFilter, Ordered{

	@Autowired
    private JwtUtil jwtUtil;
	
	

    @Override
    public Mono<Void> filter(org.springframework.web.server.ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
    	
    	 // Allow CORS preflight requests — must be first
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getURI().getPath();

        
        if ( path.contains("/auth/register") ) {
            return chain.filter(exchange);
        }
        
        
     // 🔥 LOG
        System.out.println("PATH: " + path);

        if (path.contains("/swagger-ui")
                || path.contains("/v3/api-docs")
                || path.contains("/webjars")
                || path.contains("/swagger-resources")
                || path.contains("/configuration")
                || path.endsWith(".html")
                || path.endsWith(".js")
                || path.endsWith(".css")
                || path.endsWith(".png")) {

            return chain.filter(exchange);
        }
        
        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

       
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

      
        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

      
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

       
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", username)
                .header("X-Role", role)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
