package me.igrade.user.service;

import me.igrade.user.model.Authority;

public interface AuthorityService {


    Authority getAuthorityByAuthorityName(String name);

    boolean isAuthorityByAuthorityName(String name);


    void createAuthority(String authority);

    Authority createAuthority(Authority authority);
}
