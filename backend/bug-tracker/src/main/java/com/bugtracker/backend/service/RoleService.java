package com.bugtracker.backend.service;


import com.bugtracker.backend.entity.Role;
import com.bugtracker.backend.repository.RoleRepository;
import com.bugtracker.backend.util.GlobalUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Log
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    public List<Role> getRoles(){
        return  roleRepository.getAllRole();
    }

    public List<Role> getRolesByAccessLevelAbove(int accessLevel, int pageNo){
        Pageable page = PageRequest.of(pageNo,GlobalUtil.itemsPerPage);
        return roleRepository.findAllByAccessLevelAndAbove(accessLevel,page);
    }

    public Optional<Role> getRoleById(@NonNull int id){
        return roleRepository.findById(id);
    }

    public List<Role> getRoleByRoleType(String type){
        return roleRepository.findAllByRoleType(type);
    }

    public void addAllRoles(List<Role> roles){
        roleRepository.saveAll(roles);
    }

    public String deleteRole(int id){
        roleRepository.deleteById(id);
        return "Role with id: " + id + " deleted";
    }

    public Role updateRole(Role newRole){
        Role existingRole = roleRepository.findById(newRole.getId()).orElse(null);
        existingRole.setRoleType(newRole.getRoleType());
        existingRole.setAccessLevel(newRole.getAccessLevel());
        return roleRepository.save(existingRole);
    }
}
