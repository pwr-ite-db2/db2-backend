package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Role;
import com.example.databasedemo2.entitymanagement.repositories.RoleRepository;
import com.example.databasedemo2.exceptions.custom.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, Integer> {
    private static final String DEFAULT_USER_ROLE = "CZYTELNIK";
    @Autowired
    public RoleService(RoleRepository repository) {
        super(repository);
    }

    public Role getDefaultRole() throws ResourceNotFoundException {
        return ((RoleRepository) repository).findByName(DEFAULT_USER_ROLE).orElseThrow(ResourceNotFoundException::new);
    }
}
