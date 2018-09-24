package com.epam.mentoring.d3d4.apiarchitecture.repositories;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Aliaksandr Makavetski
 */
public interface TagRepository extends CrudRepository<Tag, Long> {

	Optional<Tag> findById(long id);
}
