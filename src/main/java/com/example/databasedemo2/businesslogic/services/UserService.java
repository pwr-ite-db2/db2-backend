package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.User;
import com.example.databasedemo2.dataaccess.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService <User, Integer> implements UserDetailsService {
    @Autowired
    public UserService(JpaRepository<User, Integer> repository) {
        super(repository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ((UserRepository) repository).findByEmail(username).orElseThrow();
    }

    @Override
    protected void handle() {

    }
}
