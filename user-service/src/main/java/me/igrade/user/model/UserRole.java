package me.igrade.user.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ManyToMany
    private List<Authority> authorities;


    public UserRole(String name, List<Authority> authorities) {
        this.name = name;
        this.authorities = new ArrayList<>(authorities);
    }
    public UserRole(String name, Authority... authorities) {
        this.name = name;
        this.authorities = new ArrayList<>(List.of(authorities));
    }
}
