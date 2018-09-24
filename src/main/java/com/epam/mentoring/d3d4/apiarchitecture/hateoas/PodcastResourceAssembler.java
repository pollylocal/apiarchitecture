package com.epam.mentoring.d3d4.apiarchitecture.hateoas;


import com.epam.mentoring.d3d4.apiarchitecture.beans.Podcast;
import com.epam.mentoring.d3d4.apiarchitecture.controllers.PodcastController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * @author Aliaksandr Makavetski
 */
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class PodcastResourceAssembler extends ResourceAssemblerSupport<Podcast, PodcastResource> {

    public PodcastResourceAssembler() {
        super(PodcastController.class, PodcastResource.class);
    }

    @Override
    public PodcastResource toResource(Podcast podcast) {
        PodcastResource resource = createResourceWithId(podcast.getId(), podcast);
        resource.add(linkTo(PodcastController.class).slash(podcast.getId()).slash("tags")
                .withRel("podcast-tags"));
        return resource;
    }

    @Override
    protected PodcastResource instantiateResource(Podcast entity) {
        return new PodcastResource(entity);
    }
}
