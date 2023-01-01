package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Role;
import com.example.databasedemo2.dataaccess.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, Integer> {

    @Autowired
    public RoleService(RoleRepository repository) {
        super(repository);
    }

    @Override
    protected void handle() {

    }
}
