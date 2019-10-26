package com.example.demo.service.Impl;

import com.example.demo.entity.RoleEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleEntity findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).get();
    }
}
