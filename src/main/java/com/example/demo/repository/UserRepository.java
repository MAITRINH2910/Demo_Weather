package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    UserEntity findUserByEmail(String email);

    List<UserEntity> findAll();

    @Query(value = "SELECT * FROM ur_weather.users WHERE users.id != :userId", nativeQuery = true)
    List<UserEntity> findAllExceptAdmin( @Param("userId") Long id);

    Optional<UserEntity> findById(Long id);

    Boolean existsByUsername (String username);

    /**
     * Query to get password by user_id
     * @param password
     * @param id
     */
    @Modifying
    @Query("update UserEntity u set u.password = :password where u.id = :id")
    void updatePassword(@Param("password") String password, @Param("id") Long id);

}
