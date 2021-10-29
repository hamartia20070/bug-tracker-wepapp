package com.bugtracker.backend.controller;

import com.bugtracker.backend.entity.Role;
import com.bugtracker.backend.entity.Tag;
import com.bugtracker.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRole(){
        return roleService.getRoles();
    }

    @GetMapping("/accesslevel/{accessLevel}")
    public List<Role> getRolesByAccessLevelAbove(@PathVariable int accessLevel,@RequestParam int pageNo){
        return roleService.getRolesByAccessLevelAbove(accessLevel,pageNo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Role addRole(@RequestBody Role role){
        return roleService.saveRole(role);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public Role updateRole(@RequestBody Role role){
        return roleService.updateRole(role);
    }


    //TODO: No deletes for now
    @DeleteMapping("/{roleId}")
    public void deleteRole(@PathVariable int roleId){

    }
}
