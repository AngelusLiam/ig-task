package com.ig.demo.security;

import com.ig.demo.entity.Role;
import com.ig.demo.entity.User;
import com.ig.demo.repository.UserRepository;
import com.ig.demo.repository.UserRoleRepository;
import com.nimbusds.jose.shaded.gson.JsonArray;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component("jwtFilterRoles")
@RequiredArgsConstructor
@Profile({"default"})
@Log4j2
public class JwtFilterRoles extends OncePerRequestFilter{
	
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final ClaimConfig claimConfig;
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("Filter to URI: " + request.getRequestURI());
		if(authentication != null){
			
			Map<String, Object> claims = ((JwtAuthenticationToken) authentication).getToken().getClaims();
			
			String email = (String) claims.get(claimConfig.getEmail()) ;

			Optional<User> userOptional = userRepository.findByEmail(email);
			User user = null;
			Set<Role> ruolo = null;
			if (userOptional.isPresent()) {
				user = userOptional.get();
			} else {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			}

			List<GrantedAuthority> authorities = new ArrayList<>();
			Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			user.getUserRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getPk().getRole().getName())));
			authorities.addAll(oldAuthorities);
			SecurityContextHolder.getContext().setAuthentication(
					new CustomUsernamePasswordAuthenticationToken(
							SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
							SecurityContextHolder.getContext().getAuthentication().getCredentials(),
							authorities,
                            Math.toIntExact(user.getId()),
							user.getNome(),
							user.getCognome(),
							user.getUsername())
					);


			
		}
		filterChain.doFilter(request,response);
	}
}
