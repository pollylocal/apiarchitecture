package com.epam.mentoring.d3d4.apiarchitecture.hateoas;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Tag;
import com.epam.mentoring.d3d4.apiarchitecture.controllers.TagsController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author Aliaksandr Makavetski
 */
@Component
public class TagResourceAssembler extends ResourceAssemblerSupport<Tag, TagResource> {

    public TagResourceAssembler() {
        super(TagsController.class, TagResource.class);
    }

    @Override
    public TagResource toResource(Tag tag) {
        TagResource resource = createResourceWithId(tag.getId(), tag);
        resource.add(linkTo(TagsController.class).slash(tag.getId()).slash("podcasts")
                .withRel("tagged-podcasts"));
        return resource;
    }

    @Override
    protected TagResource instantiateResource(Tag entity) {
        return new TagResource(entity);
    }
}
