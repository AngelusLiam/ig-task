package com.ig.demo.service.impl;

import com.ig.demo.dto.RoleDTO;
import com.ig.demo.dto.UserDTO;
import com.ig.demo.entity.Role;
import com.ig.demo.entity.User;
import com.ig.demo.entity.UserRole;
import com.ig.demo.entity.UserRoleId;
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
import java.util.*;
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
        return usersPage.map(this::convertToDTO);
    }

    @Override
    public UserDTO insertUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "L'email è già in uso");
        }

        // Recupero i ruoli associati dall'elenco di nomi fornito
        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruolo non trovato: " + roleName)))
                .collect(Collectors.toSet());

        User user = convertToEntity(null, userDTO, roles);
        User u = userRepository.save(user);

        return convertToDTO(u);
    }

    @Override
    public UserDTO getUserDetail(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        User user;
        if (optUser.isPresent()){
            user = optUser.get();
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
        if (!user.getEmail().equals(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'email non può essere cambiata.");
        }

        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruolo non trovato: " + roleName)))
                .collect(Collectors.toSet());
        


        user = convertToEntity(user, userDTO, roles);
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));
        userRepository.delete(user);
    }

    private User convertToEntity(User user, UserDTO userDTO, Set<Role> roles) {
        if (user == null) {
            user = new User();
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setTaxCode(userDTO.getCodiceFiscale());
        user.setNome(userDTO.getNome());
        user.setCognome(userDTO.getCognome());

        Set<UserRole> existingUserRoles = user.getUserRoles();
        if (existingUserRoles == null) {
            existingUserRoles = new HashSet<>();
            user.setUserRoles(existingUserRoles);
        }

        Set<UserRole> updatedUserRoles = new HashSet<>();
        for (Role role : roles) {
            boolean alreadyExists = existingUserRoles.stream()
                    .anyMatch(userRole -> userRole.getPk().getRole().getId().equals(role.getId()));

            if (!alreadyExists) {
                UserRole userRole = UserRole.builder()
                        .pk(new UserRoleId(user, role))
                        .build();
                updatedUserRoles.add(userRole);
            }
        }

        existingUserRoles.addAll(updatedUserRoles);

        existingUserRoles.removeIf(userRole ->
                roles.stream().noneMatch(role -> role.getId().equals(userRole.getPk().getRole().getId()))
        );

        return user;
    }


    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .codiceFiscale(user.getTaxCode())
                .nome(user.getNome())
                .cognome(user.getCognome())
                .roles(user.getUserRoles().stream().map(r -> RoleDTO.builder()
                                .id(r.getPk().getRole().getId())
                                .name(r.getPk().getRole().getName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
