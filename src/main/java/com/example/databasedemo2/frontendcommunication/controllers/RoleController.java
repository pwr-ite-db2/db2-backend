package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.businesslogic.services.RoleService;
import com.example.databasedemo2.dataaccess.entities.Role;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleService.getAll();
    }

    @PutMapping
    public Role createOrUpdateRole(@RequestBody Role role) {
        return roleService.addOrUpdate(role);
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable("id") int roleId) {
        return roleService.getById(roleId).get();
    }

    @DeleteMapping("/{id}")
    public boolean deleteRoleById(@PathVariable("id") int roleId) {
        return roleService.deleteById(roleId);
    }
}
