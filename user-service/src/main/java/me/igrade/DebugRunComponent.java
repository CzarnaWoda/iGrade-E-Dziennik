package me.igrade;


import lombok.RequiredArgsConstructor;
import me.igrade.user.model.Authority;
import me.igrade.user.model.User;
import me.igrade.user.model.UserRole;
import me.igrade.user.service.AuthorityService;
import me.igrade.user.service.UserRoleService;
import me.igrade.user.service.UserService;
import me.igrade.user.service.impl.AuthorityServiceImpl;
import me.igrade.user.service.impl.UserRoleServiceImpl;
import me.igrade.user.service.impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DebugRunComponent implements CommandLineRunner {



    private final AuthorityService authorityService;
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {

        final Authority userAuthority = new Authority("USER");


        if(!authorityService.isAuthorityByAuthorityName("USER")){
            authorityService.createAuthority(userAuthority);
        }


        final Authority[] studentAuthorities = {
                new Authority("STUDENT_READ"),
                new Authority("STUDENT_WRITE"),
                new Authority("STUDENT_NOTIFICATIONS"),
                authorityService.getAuthorityByAuthorityName("USER")
        };


        final Authority[] teacherAuthorities = {
                new Authority("TEACHER_READ"),
                new Authority("TEACHER_WRITE"),
                new Authority("TEACHER_NOTIFICATIONS"),
                authorityService.getAuthorityByAuthorityName("USER")
        };

        for(Authority a : studentAuthorities){
            if(!authorityService.isAuthorityByAuthorityName(a.getAuthorityName())){
                authorityService.createAuthority(a);
            }
        }
        if(!userRoleService.userRoleExistByName("STUDENT")){
            final UserRole STUDENT = new UserRole("STUDENT", studentAuthorities);

            userRoleService.createUserRole(STUDENT);
        }

        for(Authority aa : teacherAuthorities){
            if(!authorityService.isAuthorityByAuthorityName(aa.getAuthorityName())){
                authorityService.createAuthority(aa);
            }
        }
        if(!userRoleService.userRoleExistByName("TEACHER")){
            final UserRole TEACHER = new UserRole("TEACHER", teacherAuthorities);

            userRoleService.createUserRole(TEACHER);
        }

        userService.updateUser(new User("Mateusz","Kint",true,passwordEncoder.encode("password"), "mateuszkint@gmail.com",1L, userRoleService.getUserRoleByName("STUDENT")));


        userService.updateUser(new User("Andrzej", "Gwóźdź",true, passwordEncoder.encode("password"),"andrzejg@gmail.com",null,userRoleService.getUserRoleByName("TEACHER")));




    }
}
