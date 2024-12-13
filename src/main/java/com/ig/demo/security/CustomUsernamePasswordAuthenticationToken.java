package com.ig.demo.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2600837770505884721L;
	
	private Integer id;
	private String nome;
	private String cognome;
	private String username;
	
	public CustomUsernamePasswordAuthenticationToken(
			Object principal, 
			Object credentials, 
			Collection<? extends GrantedAuthority> authorities, 
			Integer id,
			String nome,
			String cognome, 
			String username) {
		super(principal, credentials, authorities);
		
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
	}


}
