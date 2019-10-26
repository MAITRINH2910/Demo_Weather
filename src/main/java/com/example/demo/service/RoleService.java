package com.example.demo.service;

import com.example.demo.entity.RoleEntity;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    RoleEntity findByRoleName(String roleName);
}
