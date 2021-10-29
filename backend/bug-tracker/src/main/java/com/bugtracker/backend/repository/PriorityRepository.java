package com.bugtracker.backend.repository;


import com.bugtracker.backend.entity.Priority;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PriorityRepository extends JpaRepository<Priority,Integer>, PagingAndSortingRepository<Priority,Integer> {

    @Query("select p from Priority p")
    List<Priority> getAllPriorities(Pageable pageable);
}
