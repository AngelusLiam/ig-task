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
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
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
@Log4j2
public class JwtFilterRoles extends OncePerRequestFilter{

	@Value("${ig.security.use-internal-check}")
	boolean useInternalCheck;
	
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final ClaimConfig claimConfig;
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("Filter to URI: " + request.getRequestURI());
		if(authentication != null){
			
			Map<String, Object> claims = ((JwtAuthenticationToken) authentication).getToken().getClaims();
			List<String> roles = extractRolesFromJwt(authentication);
			User user = null;
			if (BooleanUtils.isTrue(useInternalCheck)) {
				String email = (String) claims.get(claimConfig.getEmail());

				Optional<User> userOptional = userRepository.findByEmail(email);
				if (userOptional.isPresent()) {
					user = userOptional.get();
				} else {
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
				}
			}

			List<GrantedAuthority> authorities = new ArrayList<>();
			Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			if (BooleanUtils.isTrue(useInternalCheck)) {
				if (Objects.requireNonNull(user).getUserRoles() != null && !user.getUserRoles().isEmpty()) {
					user.getUserRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getPk().getRole().getName())));
				}
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
			} else {
				roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
				authorities.addAll(oldAuthorities);
				SecurityContextHolder.getContext().setAuthentication(
						new CustomUsernamePasswordAuthenticationToken(
								SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
								SecurityContextHolder.getContext().getAuthentication().getCredentials(),
								authorities)
				);
			}
		}
		filterChain.doFilter(request,response);
	}

	public List<String> extractRolesFromJwt(Authentication authentication) {
		Map<String, Object> claims = ((JwtAuthenticationToken) authentication).getToken().getClaims();
		Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");

		if (realmAccess != null) {
			return (List<String>) realmAccess.get("roles");
		}
		return List.of();
	}

}
