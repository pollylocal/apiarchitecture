package com.epam.mentoring.d3d4.apiarchitecture.controllers;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Podcast;
import com.epam.mentoring.d3d4.apiarchitecture.beans.PodcastInput;
import com.epam.mentoring.d3d4.apiarchitecture.beans.Tag;
import com.epam.mentoring.d3d4.apiarchitecture.exception.ResourceDoesNotExistException;
import com.epam.mentoring.d3d4.apiarchitecture.hateoas.*;
import com.epam.mentoring.d3d4.apiarchitecture.repositories.PodcastRepository;
import com.epam.mentoring.d3d4.apiarchitecture.service.EntityFromURIExtractor;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping("/podcasts")
public class PodcastController {

    private final PodcastRepository podcastRepository;
    private final PodcastResourceAssembler podcastResourceAssembler;
    private final TagResourceAssembler tagResourceAssembler;
    private final EntityFromURIExtractor<Tag> tagFromURIExtractor;

    public PodcastController(PodcastRepository podcastRepository,
                             PodcastResourceAssembler podcastResourceAssembler,
                             TagResourceAssembler tagResourceAssembler,
                             @Qualifier("tagExtractor") EntityFromURIExtractor<Tag> tagFromURIExtractor) {
        this.podcastRepository = podcastRepository;
        this.podcastResourceAssembler = podcastResourceAssembler;
        this.tagResourceAssembler = tagResourceAssembler;
        this.tagFromURIExtractor = tagFromURIExtractor;
    }

    @GetMapping
    NestedContentResource<PodcastResource> all() {
        return new NestedContentResource<PodcastResource>(podcastResourceAssembler.toResources(podcastRepository.findAll()));
    }

    @GetMapping(value = "/{id}")
    Resource<Podcast> one(@PathVariable("id") long id) {
        Podcast podcast = findById(id);
        return this.podcastResourceAssembler.toResource(podcast);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    HttpHeaders create(@RequestBody PodcastInput podcastInput) {
        Podcast podcast = new Podcast(podcastInput);
        podcast.setTags(tagFromURIExtractor.getEntities(podcastInput.getTagUris()));
        podcastRepository.save(podcast);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(linkTo(PodcastController.class).slash(podcast.getId()).toUri());
        return httpHeaders;
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable("id") long id, @RequestBody PodcastInput podcastInput) {
        Podcast podcast = findById(id);
        if (podcastInput.getTitle() != null) {
            podcast.setTitle(podcastInput.getTitle());
        }
        if (podcastInput.getDescription() != null) {
            podcast.setDescription(podcastInput.getDescription());
        }
        if (podcastInput.getTagUris()!= null) {
            podcast.setTags(tagFromURIExtractor.getEntities(podcastInput.getTagUris()));
        }
        podcastRepository.save(podcast);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable("id") long id) {
        podcastRepository.deleteById(id);
    }

    @GetMapping(value = "/{id}/tags")
    ResourceSupport podcastTags(@PathVariable("id") long id) {
        return new NestedContentResource<TagResource>(
                this.tagResourceAssembler.toResources(findById(id).getTags()));
    }

    private Podcast findById(@PathVariable("id") long id) {
        return podcastRepository.findById(id).orElseThrow(ResourceDoesNotExistException::new);
    }
}
