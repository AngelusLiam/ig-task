package com.ig.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private Set<RoleDTO> roles;
}
