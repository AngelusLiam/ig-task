package com.ig.demo.repository;

import com.ig.demo.entity.Role;
import com.ig.demo.entity.UserRole;
import com.ig.demo.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

}
