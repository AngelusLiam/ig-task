package com.ig.demo.service;

import com.ig.demo.dto.UserDTO;
import com.ig.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO insertUser(UserDTO userDTO);
    UserDTO getUserDetail(Long id);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long id);


}
