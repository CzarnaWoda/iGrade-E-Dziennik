package me.igrade.user.service.impl;

import lombok.RequiredArgsConstructor;
import me.igrade.user.model.Authority;
import me.igrade.user.model.UserRole;
import me.igrade.user.repository.UserRoleRepository;
import me.igrade.user.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public UserRole getUserRoleByName(String name){
        return userRoleRepository.getUserRoleByName(name);
    }

    @Override
    public boolean userRoleExistByName(String name){
        return userRoleRepository.existsByName(name);
    }
    @Override
    public void createUserRole(String roleName){
        userRoleRepository.save(new UserRole(roleName, Collections.emptyList()));
    }
    @Override
    public void createUserRole(String roleName, Authority... authorities){
        userRoleRepository.save(new UserRole(roleName,authorities));
    }
    @Override
    public void createUserRole(UserRole userRole){
        userRoleRepository.save(userRole);
    }

}
