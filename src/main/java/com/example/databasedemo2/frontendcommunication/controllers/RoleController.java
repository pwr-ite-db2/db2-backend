package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.services.RoleService;
import com.example.databasedemo2.entitymanagement.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public List<Role> getRoles(@RequestParam(required = false) Map<String, String> params) {
        return roleService.getAll(params);
    }

    @PutMapping
    public Role createOrUpdateRole(@RequestBody Role role, @RequestParam(required = false) Map<String, String> params) {
        return roleService.addOrUpdate(role, params);
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable("id") int roleId) {
        return roleService.getById(roleId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteRoleById(@PathVariable("id") int roleId) {
        return roleService.deleteById(roleId);
    }
}