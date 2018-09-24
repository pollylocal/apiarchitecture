package com.epam.mentoring.d3d4.apiarchitecture.hateoas;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Tag;
import org.springframework.hateoas.Resource;

/**
 * @author Aliaksandr Makavetski
 */
public class TagResource extends Resource<Tag> {

    public TagResource(Tag content) {
        super(content);
    }
}