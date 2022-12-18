package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Tag;
import com.example.databasedemo2.dataaccess.repositories.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag addOrUpdateTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(int id) {
        return tagRepository.findById(id).orElseThrow();
    }

    public boolean deleteTagById(int id) {
        tagRepository.deleteById(id);
        return true;
    }
}
