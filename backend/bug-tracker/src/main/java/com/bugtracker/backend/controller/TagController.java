package com.bugtracker.backend.controller;

import com.bugtracker.backend.entity.Priority;
import com.bugtracker.backend.entity.Tag;
import com.bugtracker.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> getAllTags(){
        return tagService.getAllTags();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag addTag(@RequestBody Tag tag){
        return tagService.saveTag(tag);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Tag updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    //TODO No delete for now
    @DeleteMapping("/{tagId}")
    public void deleteTag(@PathVariable int tagId){

    }
}
