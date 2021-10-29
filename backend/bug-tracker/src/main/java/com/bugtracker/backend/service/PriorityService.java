package com.bugtracker.backend.service;

import com.bugtracker.backend.entity.Priority;
import com.bugtracker.backend.repository.PriorityRepository;
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
public class PriorityService {

    @Autowired
    private PriorityRepository priorityRepository;

    public Optional<Priority> getPriorityById(@NonNull int priorityId){
        return priorityRepository.findById(priorityId);
    }

    public void addAllPriorities(List<Priority> priorities){
        priorityRepository.saveAll(priorities);
    }

    public List<Priority> getAllPriorities(int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return priorityRepository.getAllPriorities(page);
    }

    public List<Priority> getAllPriorities(){
        return priorityRepository.findAll();
    }


    public Priority savePriority(Priority priority){
        return priorityRepository.save(priority);
    }

    public Priority updatePriority(Priority newPriority){
        Priority existingPriority= priorityRepository.findById(newPriority.getId()).orElse(null);
        existingPriority.setPriorityType(newPriority.getPriorityType());
        existingPriority.setImportance(newPriority.getImportance());

        return priorityRepository.save(existingPriority);

    }

}
