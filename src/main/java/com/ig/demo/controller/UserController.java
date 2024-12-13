package com.ig.demo.controller;

import com.ig.demo.dto.UserDTO;
import com.ig.demo.security.CustomUsernamePasswordAuthenticationToken;
import com.ig.demo.service.IUserService;
import com.ig.demo.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@SecurityScheme(
        ref = "AUTHORIZATION",
        type = SecuritySchemeType.APIKEY
)
@CrossOrigin
@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("users")
public class UserController {

    private final IUserService userService;

    @Operation(
            operationId = "getAllUsers",
            description = "Restituisce tutti gli utenti"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_USER')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        log.info("[AuthenticatedUser: {}] || [UserController] getAllUsers", SecurityUtils.getLoggedUsername());
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(
            operationId = "getUser",
            description = "Restituisce il dettaglio di un utente"
    )
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_USER')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        log.info("[AuthenticatedUser: {}] || [UserController] getUser", SecurityUtils.getLoggedUsername());
        return ResponseEntity.ok(userService.getUserDetail(id));
    }

    @Operation(
            operationId = "insertUser",
            description = "Inserisce un nuovo utente"
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO dto){
        log.info("[AuthenticatedUser: {}] || [UserController] insertUser", SecurityUtils.getLoggedUsername());
        return ResponseEntity.ok(userService.insertUser(dto));
    }

    @Operation(
            operationId = "updateUser",
            description = "Aggiorna un utente"
    )
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody UserDTO dto){
        log.info("[AuthenticatedUser: {}] || [UserController] updateUser", SecurityUtils.getLoggedUsername());
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @Operation(
            operationId = "delete",
            description = "Cancella un utente"
    )
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_OWNER', 'ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        log.info("[AuthenticatedUser: {}] || [UserController] deleteUser", SecurityUtils.getLoggedUsername());
        userService.deleteUser(id);
        return ResponseEntity.ok("Success");
    }

}
