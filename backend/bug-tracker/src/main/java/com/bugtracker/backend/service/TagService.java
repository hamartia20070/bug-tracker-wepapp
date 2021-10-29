package com.bugtracker.backend.service;

import com.bugtracker.backend.entity.Tag;
import com.bugtracker.backend.entity.Ticket;
import com.bugtracker.backend.repository.TagRepository;
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
import java.util.Set;

@Service
@Log
@Transactional
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TicketService ticketService;

    public Optional<Tag> getTagById(@NonNull int id){
        return tagRepository.findById(id);
    }

    public List<Tag> getAllTags(){
        return tagRepository.getAllTag();
    }

    public List<Tag> getTagByName(String tag){
        return tagRepository.findAllByTag(tag);
    }

    public void addAllTags(List<Tag> tags){
        tagRepository.saveAll(tags);
    }



    public Tag saveTag(Tag tag){
        return tagRepository.save(tag);
    }

    public Tag updateTag(Tag newTag){
        Tag existingTag = tagRepository.findById(newTag.getId()).orElse(null);
        existingTag.setTag(newTag.getTag());

        return tagRepository.save(existingTag);
    }
}
