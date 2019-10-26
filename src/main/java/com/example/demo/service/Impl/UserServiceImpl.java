package com.example.demo.service.Impl;

import com.example.demo.DTO.display.UserRegistrationDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WeatherEntity;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WeatherRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${host.http}")
    private String host_http;
    @Value("${domain}")
    private String domain_http;
    @Value("${tail.icon.path}")
    private String tail_icon_path;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<UserEntity> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public UserEntity saveUserDto(UserRegistrationDto accountDto) {
        UserEntity user = new UserEntity();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setUsername(accountDto.getUsername());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        HashSet<RoleEntity> userRole = new HashSet<>();
        userRole.add(roleRepository.findByRoleName("ROLE_USER").get());
        user.setRoleName(userRole);
        user.setActive(true);
        return userRepository.save(user);
    }
    //    @Override
//    public User saveUser(User admin) {
//        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
//        admin.setActive(true);
//        HashSet<Roles> roles = new HashSet<>();
//        roles.add(roleRepository.findByRoleName("ROLE_ADMIN"));
////        roles.add(roleRepository.findByRoleName("ROLE_USER"));
//        admin.setRoleName(roles);
//        return userRepository.save(admin);
//    }

    @Override
    public void editStatusUser(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (userRepository.findById(id).get().isActive()) {
            user.get().setActive(false);
        } else {
            user.get().setActive(true);
        }
        userRepository.save(user.get());
    }

    @Override
    public void editRoleUser(Long id, String roleName) {
        Optional<UserEntity> user = userRepository.findById(id);
        RoleEntity role = roleRepository.findByRoleName(roleName).get();
        if (role.getRoleName().equals("ROLE_ADMIN")) {
            HashSet<RoleEntity> userRole = new HashSet<>();
            userRole.add(roleRepository.findByRoleName("ROLE_ADMIN").get());
            userRole.remove(roleRepository.findByRoleName("ROLE_USER"));
            user.get().setRoleName(userRole);
            userRepository.save(user.get());
        } else if (role.getRoleName().equals("ROLE_USER")) {
            HashSet<RoleEntity> userRole = new HashSet<>();
            userRole.remove(roleRepository.findByRoleName("ROLE_ADMIN"));
            userRole.add(roleRepository.findByRoleName("ROLE_USER").get());
            user.get().setRoleName(userRole);
            userRepository.save(user.get());
        }
    }

    @Override
    public void updatePassword(String password, Long userId) {
        userRepository.updatePassword(password, userId);
    }

    /**
     * Get Authentication User from Security Context Holder of Spring Security
     * Set icon of weather for user
     *
     * @return
     */
    @Override
    public UserEntity getAuthUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUsername(authUser.getName());
        List<WeatherEntity> weatherList = weatherRepository.findAllByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(host_http + domain_http + "/img/w/" + weatherList.get(i).getIcon() + tail_icon_path);
        }
        return user;
    }

    /**
     * Get Authentication User from Security Context Holder of Spring Security
     *
     * @return
     */
    @Override
    public UserEntity getUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUsername(authUser.getName());
        return user;
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return userRepository.existsByUsername(userName);
    }
}
