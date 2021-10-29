package com.bugtracker.backend.controller;

import com.bugtracker.backend.entity.Priority;
import com.bugtracker.backend.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/priorities")
public class PriorityController {

    @Autowired
    private PriorityService priorityService;

    @GetMapping
    public List<Priority> getAllPriorities(){
        return priorityService.getAllPriorities();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Priority addPriority(@RequestBody Priority priority){
        return priorityService.savePriority(priority);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public Priority updatePriority(@RequestBody Priority priority){
        return priorityService.updatePriority(priority);
    }


    //TODO: no deletes for now
    @DeleteMapping("/{priorityId}")
    public void deletePriority(@PathVariable int priorityId){

    }

}
