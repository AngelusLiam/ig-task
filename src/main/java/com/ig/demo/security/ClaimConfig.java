package com.ig.demo.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
@ConfigurationProperties(prefix = "ig.security.jwt")
public class ClaimConfig {

	private String upnClaim;
	private String familyName;
	private String nameClaim;
	private String email;
	private List<String> roles;

}
