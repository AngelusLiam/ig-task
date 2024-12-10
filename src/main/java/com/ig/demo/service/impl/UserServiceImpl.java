package com.ig.demo.service.impl;

import com.ig.demo.dto.RoleDTO;
import com.ig.demo.dto.UserDTO;
import com.ig.demo.entity.Role;
import com.ig.demo.entity.User;
import com.ig.demo.repository.RoleRepository;
import com.ig.demo.repository.UserRepository;
import com.ig.demo.repository.UserRoleRepository;
import com.ig.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        usersPage.getContent().forEach(e -> {
            e.setRoles(userRoleRepository.findRolesByUserId(e.getId()));
        });
        return usersPage.map(this::convertToDTO);  // Mappa ogni User a UserDTO
    }

    @Override
    public UserDTO insertUser(UserDTO userDTO) {
        // Controllo se l'email è già presente
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "L'email è già in uso");
        }

        // Recupero i ruoli associati dall'elenco di nomi fornito
        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruolo non trovato: " + roleName)))
                .collect(Collectors.toSet());

        User user = convertToEntity(userDTO, roles);
        User u = userRepository.save(user);

        return convertToDTO(u);
    }

    @Override
    public UserDTO getUserDetail(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        User user;
        if (optUser.isPresent()){
            user = optUser.get();
            user.setRoles(userRoleRepository.findRolesByUserId(user.getId()));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non presente nei sistemi");
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));

        if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "L'email è già in uso");
        }

        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruolo non trovato: " + roleName)))
                .collect(Collectors.toSet());

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setCodiceFiscale(userDTO.getCodiceFiscale());
        user.setNome(userDTO.getNome());
        user.setCognome(userDTO.getCognome());
        user.setRoles(roles);

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));
        userRepository.delete(user);
        userRoleRepository.deleteByUserId(id);
    }

    private User convertToEntity(UserDTO userDTO, Set<Role> roles){
        return User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .codiceFiscale(userDTO.getCodiceFiscale())
                .nome(userDTO.getNome())
                .cognome(userDTO.getCognome())
                .roles(roles)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .codiceFiscale(user.getCodiceFiscale())
                .nome(user.getNome())
                .cognome(user.getCognome())
                .roles(user.getRoles().stream().map(r -> RoleDTO.builder()
                                .id(r.getId())
                                .name(r.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
