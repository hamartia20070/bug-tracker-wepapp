package com.bugtracker.backend.repository;

import com.bugtracker.backend.entity.Project;
import com.bugtracker.backend.entity.Role;
import com.bugtracker.backend.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Integer>, PagingAndSortingRepository<Tag,Integer> {

    @Query("select t from Tag t where t.tag = ?1")
    List<Tag> findAllByTag(String tag);

    @Query("select t from Tag t")
    List<Tag> getAllTag();
}
