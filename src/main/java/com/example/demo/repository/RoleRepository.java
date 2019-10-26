package com.example.demo.repository;

import com.example.demo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Role Repository connect to Database
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

//    RoleEntity findByRoleName(String roleName);

    Optional<RoleEntity> findByRoleName(String roleName);

}
