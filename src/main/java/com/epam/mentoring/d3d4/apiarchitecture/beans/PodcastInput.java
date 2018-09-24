package com.epam.mentoring.d3d4.apiarchitecture.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.List;

/**
 * @author Aliaksandr Makavetski
 */
public class PodcastInput {
    private final String title;
    private final String description;
    private final List<URI> tagUris;

    @JsonCreator
    public PodcastInput(@JsonProperty("title") String title,
                        @JsonProperty("description") String description,
                        @JsonProperty("tags") List<URI> tagUris) {
        this.title = title;
        this.description = description;
        this.tagUris = tagUris;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<URI> getTagUris() {
        return tagUris;
    }
}
