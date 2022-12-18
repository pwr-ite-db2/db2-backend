package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Role;
import com.example.databasedemo2.dataaccess.repositories.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, Integer> {
//    public RoleService(TestRepository repository) {
//        super(repository);
//    }

    public RoleService(BaseRepository<Role, Integer> repository) {
        super(repository);
    }
}
