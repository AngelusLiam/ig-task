package com.ig.demo.config;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class WebSecurityConfig {

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	String issuerUri;
	
	@Autowired()
	@Qualifier("jwtFilterRoles")
	private OncePerRequestFilter jwtRequestFilterRoles;
	
	private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/actuator/**",
			"/auth/**"
    };	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtDecoder decoder) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize
								.requestMatchers(AUTH_WHITELIST).permitAll()
								.anyRequest().authenticated()
				)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(decoder))
                )
                .addFilterAfter(jwtRequestFilterRoles, BearerTokenAuthenticationFilter.class)
				;
			return httpSecurity.build();
	}
	
	@Bean
	public JwtDecoder decoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }
}
