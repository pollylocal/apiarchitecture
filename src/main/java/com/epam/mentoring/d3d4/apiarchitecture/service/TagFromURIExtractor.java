package com.epam.mentoring.d3d4.apiarchitecture.service;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Tag;
import com.epam.mentoring.d3d4.apiarchitecture.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aliaksandr Makavetski
 */
@Component
@Qualifier("tagExtractor")
public class TagFromURIExtractor implements EntityFromURIExtractor<Tag> {
    private static final UriTemplate TAG_URI_TEMPLATE = new UriTemplate("/tags/{id}");
    private final TagRepository tagRepository;

    public TagFromURIExtractor(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getEntities(List<URI> tagLocations) {
        List<Tag> tags = new ArrayList<>(tagLocations.size());
        for (URI tagLocation : tagLocations) {
            Tag tag = this.tagRepository.findById(getId(tagLocation))
                    .orElseThrow(() -> new IllegalArgumentException("The tag '" + tagLocation + "' does not exist"));
            tags.add(tag);
        }
        return tags;
    }

    private long getId(URI location) {
        try {
            String idString = TAG_URI_TEMPLATE.match(location.toASCIIString()).get(
                    "id");
            return Long.valueOf(idString);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("The tag '" + location + "' is invalid");
        }
    }
}
