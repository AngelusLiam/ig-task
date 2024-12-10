package com.ig.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private Set<RoleDTO> roles;
}
