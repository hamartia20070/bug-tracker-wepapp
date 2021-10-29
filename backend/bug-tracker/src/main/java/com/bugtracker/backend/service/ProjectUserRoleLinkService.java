package com.bugtracker.backend.service;

import com.bugtracker.backend.entity.Project;
import com.bugtracker.backend.entity.ProjectUserRoleLink;
import com.bugtracker.backend.entity.Role;
import com.bugtracker.backend.entity.User;
import com.bugtracker.backend.repository.ProjectUserRoleLinkRepository;
import com.bugtracker.backend.util.GlobalUtil;
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
public class ProjectUserRoleLinkService {

    @Autowired
    private ProjectUserRoleLinkRepository projectUserRoleLinkRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RoleService roleService;

    public List<ProjectUserRoleLink> getAllLinks(int pageNo){
        Pageable page = PageRequest.of(pageNo,GlobalUtil.itemsPerPage);
        return projectUserRoleLinkRepository.getAll(page);
    }

    public Optional<ProjectUserRoleLink> addUserToProject(int userId, int projectId, int roleId){
        Project project = projectService.getProjectById(projectId).orElse(null);
        User user = userService.getUserById(userId).orElse(null);
        Role role = roleService.getRoleById(roleId).orElse(null);

        ProjectUserRoleLink newLink = new ProjectUserRoleLink(project,user,role);

        return Optional.of(projectUserRoleLinkRepository.save(newLink));
    }

    public Optional<ProjectUserRoleLink> updateUserRoleInProject(int projectId,int userId,int newRoleId){

        List<ProjectUserRoleLink> link = projectUserRoleLinkRepository.findAllByUserAndProject(userId,projectId);
        Role newRole = roleService.getRoleById(newRoleId).orElse(null);

        for(ProjectUserRoleLink intLink:link){
            intLink.setRole(newRole);

            return Optional.of(projectUserRoleLinkRepository.save(intLink));
        }
        return null;

    }

    public String removeUserFromProject(int projectId,int userId){
        List<ProjectUserRoleLink> links= projectUserRoleLinkRepository.findAllByUserAndProject(userId,projectId);

        for(ProjectUserRoleLink intLink:links){
            projectUserRoleLinkRepository.delete(intLink);
        }

        return "User "+userId+" Removed from project "+projectId;
    }


    public List<ProjectUserRoleLink> getLinksOfProjectAndUser(int projectId, int userId){
        return projectUserRoleLinkRepository.findAllByUserAndProject(userId,projectId);
    }
    public ProjectUserRoleLink getLinkProjectAndUser(int projectId,int userId){
        return projectUserRoleLinkRepository.findByUserAndProject(userId,projectId);
    }

    public List<User> findAllUserByProject(int projectId, int pageNo){
        Pageable page = PageRequest.of(pageNo,GlobalUtil.itemsPerPage);
        return projectUserRoleLinkRepository.findAllUsersByProject(projectId,page);
    }


}
