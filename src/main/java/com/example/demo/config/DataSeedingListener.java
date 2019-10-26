package com.example.demo.config;

import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * The DataSeedingListener class is used to illustrate:
 * 1. Create Default Admin for the first time start server
 * This allows MyListener to be notified when the context has refreshed
 * One can use that to run arbitrary code when the application context has fully started
 *  Với tham số ContextRefreshedEvent, đoạn code trong onApplicationEvent() sẽ được publish khi Spring Context start hoặc refresh.
 */
@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${account.admin.user}")
    private String userAdmin;

    @Value("${account.admin.password}")
    private String passwordAdmin;

    @Value("${my.email}")
    private String emaildAdmin;

    @Value("${admin.firstName}")
    private String firstName;

    @Value("${admin.lastName}")
    private String lastName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Set Role entity
        // Check ROLE_ADMIN have been existed
        if (!roleRepository.findByRoleName(Constants.ADMIN).isPresent()) {
            // If result = false, Create ROLE_ADMIN
            roleRepository.save(new RoleEntity(Constants.ADMIN));
        }
        // Check ROLE_USER have been existed
        if (!roleRepository.findByRoleName(Constants.USER).isPresent()) {
            // If result = false, Create ROLE_USER
            roleRepository.save(new RoleEntity(Constants.USER));
        }

        // Set account admin
        // Check userAdmin have been existed
        if (!userRepository.existsByUsername(userAdmin)) {
            // Create one UserEntity Admin
            UserEntity accountAdmin = new UserEntity();
            // Set property for User has ROLE_ADMIN
            accountAdmin.setUsername(userAdmin);
            accountAdmin.setEmail(emaildAdmin);
            accountAdmin.setPassword(passwordEncoder.encode(passwordAdmin));
            accountAdmin.setFirstName(firstName);
            accountAdmin.setLastName(lastName);
            accountAdmin.setActive(Constants.ACTIVE);
            HashSet<RoleEntity> userRole = new HashSet<>();
            userRole.add(roleRepository.findByRoleName("ROLE_ADMIN").get());
            accountAdmin.setRoleName(userRole);
            //Add USER Admin in DB
            userRepository.save(accountAdmin);

        }
    }

}