package me.igrade.user.service;

import me.igrade.user.model.Authority;
import me.igrade.user.model.UserRole;


public interface UserRoleService {

    UserRole getUserRoleByName(String name);
    boolean userRoleExistByName(String name);
    void createUserRole(String roleName);
    void createUserRole(String roleName, Authority... authorities);
    void createUserRole(UserRole userRole);
}
