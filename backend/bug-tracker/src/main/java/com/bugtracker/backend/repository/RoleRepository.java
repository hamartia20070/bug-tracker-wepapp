package com.bugtracker.backend.repository;

import com.bugtracker.backend.entity.Project;
import com.bugtracker.backend.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer>, PagingAndSortingRepository<Role,Integer> {

    @Query("select r from Role r where r.roleType = ?1")
    List<Role> findAllByRoleType(String roleType);

    @Query("select r from Role r")
    List<Role> getAllRole();

    @Query("select r from Role r where r.accessLevel >= ?1")
    List<Role> findAllByAccessLevelAndAbove(int accessLevel, Pageable pageable);
}
