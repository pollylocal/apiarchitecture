package com.epam.mentoring.d3d4.apiarchitecture.hateoas;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author Aliaksandr Makavetski
 */
public class NestedContentResource<T> extends ResourceSupport {

    private final Resources<T> nested;

    public NestedContentResource(Iterable<T> toNest) {
        this.nested = new Resources<T>(toNest);
    }

    @JsonUnwrapped
    public Resources<T> getNested() {
        return this.nested;
    }
}
