package com.ig.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDTO {
    private Long id;
    private String name;
}
