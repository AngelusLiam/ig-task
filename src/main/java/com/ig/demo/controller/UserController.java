package com.ig.demo.controller;

import com.ig.demo.dto.UserDTO;
import com.ig.demo.service.IUserService;
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

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        log.info("[UserController] getAllUsers");
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        log.info("[UserController] getUser");
        return ResponseEntity.ok(userService.getUserDetail(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO dto){
        log.info("[UserController] insertUser");
        return ResponseEntity.ok(userService.insertUser(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody UserDTO dto){
        log.info("[UserController] updateUser");
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        log.info("[UserController] deleteUser");
        userService.deleteUser(id);
        return ResponseEntity.ok("Success");
    }

}
