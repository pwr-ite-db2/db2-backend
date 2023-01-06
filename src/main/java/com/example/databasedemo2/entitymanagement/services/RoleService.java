package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Role;
import com.example.databasedemo2.entitymanagement.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, Integer> {
    @Autowired
    public RoleService(RoleRepository repository) {
        super(repository);
    }

    public Role getDefaultRole() {
        return ((RoleRepository) repository).findByName("CZYTELNIK").orElseThrow();
    }
}
