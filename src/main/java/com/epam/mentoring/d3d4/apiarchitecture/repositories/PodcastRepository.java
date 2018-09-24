package com.epam.mentoring.d3d4.apiarchitecture.repositories;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Podcast;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Aliaksandr Makavetski
 */
public interface PodcastRepository extends CrudRepository<Podcast, Long> {
    Optional<Podcast> findById(long id);
}
