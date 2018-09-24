
package com.epam.mentoring.d3d4.apiarchitecture.controllers;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Podcast;
import com.epam.mentoring.d3d4.apiarchitecture.beans.Tag;
import com.epam.mentoring.d3d4.apiarchitecture.beans.TagInput;
import com.epam.mentoring.d3d4.apiarchitecture.exception.ResourceDoesNotExistException;
import com.epam.mentoring.d3d4.apiarchitecture.hateoas.NestedContentResource;
import com.epam.mentoring.d3d4.apiarchitecture.hateoas.PodcastResourceAssembler;
import com.epam.mentoring.d3d4.apiarchitecture.hateoas.TagResource;
import com.epam.mentoring.d3d4.apiarchitecture.hateoas.TagResourceAssembler;
import com.epam.mentoring.d3d4.apiarchitecture.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author Aliaksandr Makavetski
 */
@RestController
@RequestMapping("tags")
public class TagsController {

    private final TagRepository tagRepository;

    private final PodcastResourceAssembler podcastResourceAssembler;

    private final TagResourceAssembler tagResourceAssembler;

    @Autowired
    public TagsController(TagRepository tagRepository,
                          PodcastResourceAssembler podcastResourceAssembler,
                          TagResourceAssembler tagResourceAssembler) {
        this.tagRepository = tagRepository;
        this.podcastResourceAssembler = podcastResourceAssembler;
        this.tagResourceAssembler = tagResourceAssembler;
    }

    @GetMapping
    NestedContentResource<TagResource> all() {
        return new NestedContentResource<>(
                this.tagResourceAssembler.toResources(this.tagRepository.findAll()));
    }

    @GetMapping(value = "/{id}")
    Resource<Tag> one(@PathVariable("id") long id) {
        Tag tag = findById(id);
        return this.tagResourceAssembler.toResource(tag);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    HttpHeaders create(@RequestBody TagInput tagInput) {
        Tag tag = new Tag(tagInput);
        this.tagRepository.save(tag);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(linkTo(TagsController.class).slash(tag.getId()).toUri());
        return httpHeaders;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}")
    void update(@PathVariable("id") long id, @RequestBody TagInput tagInput) {
        Tag tag = findById(id);
        if (tagInput.getName() != null) {
            tag.setName(tagInput.getName());
        }
        this.tagRepository.save(tag);
    }


    @GetMapping(value = "/{id}/podcasts")
    ResourceSupport tagPodcasts(@PathVariable("id") long id) {
        Tag tag = findById(id);
        return new NestedContentResource<>(this.podcastResourceAssembler.toResources(tag.getPodcasts()));
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable("id") long id) {
        this.tagRepository.deleteById(id);
    }

    private Tag findById(long id) {
        return tagRepository.findById(id).orElseThrow(ResourceDoesNotExistException::new);
    }
}
