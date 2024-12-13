package com.ig.demo.controller;

import com.ig.demo.security.CustomUsernamePasswordAuthenticationToken;
import com.ig.demo.service.JwtService;
import com.ig.demo.service.impl.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserAuthService userService;
	private final JwtService jwtService;

	@Operation(
			operationId = "getToken",
			description = "Recupera il token"
	)
	@PostMapping(value = "/token")
	public ResponseEntity<String> getToken(@RequestParam String username, @RequestParam String password) {
		try {
			String token = jwtService.fetchToken(username, password);
			return ResponseEntity.ok(token);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching token: " + e.getMessage());
		}
	}

	@Operation(
			operationId = "getUserInfo",
			description = "Recupera le informazioni utente"
	)
    @GetMapping("")
    public ResponseEntity<UserInfoDTO> getUserInfo() throws Exception{
        
    	CustomUsernamePasswordAuthenticationToken authenticationToken = userService.getCustomAuthentication();
        String ruolo = authenticationToken.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(r->r.startsWith("ROLE_"))
                .collect(Collectors.joining(", ")).substring(5);
        
        return new ResponseEntity<>(new UserInfoDTO(authenticationToken.getNome(), 
        											authenticationToken.getCognome(), 
        											authenticationToken.getUsername(),
        											ruolo),
        		HttpStatus.OK); 
    }
    
    @Data
    @AllArgsConstructor
    private class UserInfoDTO {
    	String nome;
    	String cognome;
    	String username;
    	String ruolo;
    }
}

