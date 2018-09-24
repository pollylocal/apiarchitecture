package com.epam.mentoring.d3d4.apiarchitecture.service;

import java.net.URI;
import java.util.List;

/**
 * @author Aliaksandr Makavetski
 */
public interface EntityFromURIExtractor<T> {
    List<T> getEntities(List<URI> locations);
}
