package me.igrade.user.service.impl;

import lombok.RequiredArgsConstructor;
import me.igrade.user.model.Authority;
import me.igrade.user.repository.AuthorityRepository;
import me.igrade.user.service.AuthorityService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityByAuthorityName(String name){
        return authorityRepository.getAuthorityByAuthorityName(name);
    }

    @Override
    public boolean isAuthorityByAuthorityName(String name) {
        return authorityRepository.existsByAuthorityName(name);
    }

    @Override
    public void createAuthority(String authority){
        authorityRepository.save(new Authority(authority));
    }
    @Override
    public Authority createAuthority(Authority authority){
        return authorityRepository.save(authority);
    }

}
