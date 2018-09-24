package com.epam.mentoring.d3d4.apiarchitecture.hateoas;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Podcast;
import org.springframework.hateoas.Resource;

/**
 * @author Aliaksandr Makavetski
 */
public class PodcastResource extends Resource<Podcast> {

    public PodcastResource(Podcast content) {
        super(content);
    }
}
