package com.ig.demo.controller;

import com.ig.demo.dto.UserDTO;
import com.ig.demo.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        log.info("[UserController] getAllUsers");
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(
            operationId = "getUser",
            description = "Restituisce il dettaglio di un utente"
    )
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        log.info("[UserController] getUser");
        return ResponseEntity.ok(userService.getUserDetail(id));
    }

    @Operation(
            operationId = "insertUser",
            description = "Inserisce un nuovo utente"
    )
    @PostMapping
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO dto){
        log.info("[UserController] insertUser");
        return ResponseEntity.ok(userService.insertUser(dto));
    }

    @Operation(
            operationId = "updateUser",
            description = "Aggiorna un utente"
    )
    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody UserDTO dto){
        log.info("[UserController] updateUser");
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @Operation(
            operationId = "delete",
            description = "Cancella un utente"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        log.info("[UserController] deleteUser");
        userService.deleteUser(id);
        return ResponseEntity.ok("Success");
    }

}
