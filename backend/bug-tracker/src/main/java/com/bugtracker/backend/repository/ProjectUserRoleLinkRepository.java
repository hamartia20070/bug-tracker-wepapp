package com.bugtracker.backend.repository;

import com.bugtracker.backend.entity.Project;
import com.bugtracker.backend.entity.ProjectUserRoleLink;
import com.bugtracker.backend.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUserRoleLinkRepository extends JpaRepository<ProjectUserRoleLink,Integer>,
        JpaSpecificationExecutor<ProjectUserRoleLink>, PagingAndSortingRepository<ProjectUserRoleLink,Integer> {

    @Query("select p from ProjectUserRoleLink p ")
    List<ProjectUserRoleLink> getAll(Pageable pageable);

    @Query("select p from ProjectUserRoleLink p where p.user = ?1")
    List<Project> findAllByUser(int idUser);

    @Query("select p from ProjectUserRoleLink p where p.user.id = ?1 and p.role.id = ?2")
    List<ProjectUserRoleLink> findAllByUserAndRole(int userId, int roleId);

    @Query("select p from ProjectUserRoleLink p where p.user.id = ?1 and p.project.id = ?2")
    List<ProjectUserRoleLink> findAllByUserAndProject(int user, int project);

    @Query("select p from ProjectUserRoleLink p where p.user.id = ?1 and p.project.id = ?2")
    ProjectUserRoleLink findByUserAndProject(int user, int project);

    @Query("select p.user from ProjectUserRoleLink p where p.project.id = ?1")
    List<User> findAllUsersByProject(int projectId, Pageable pageable);



}
