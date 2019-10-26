package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class RoleEntity {
    public RoleEntity(String roleName) {
        this.roleName = roleName;
    }

    public RoleEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "roleName")
    private List<UserEntity> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String name) {
        this.roleName = roleName;
    }


}