package com.epam.mentoring.d3d4.apiarchitecture.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Aliaksandr Makavetski
 */
public class TagInput {

	private final String name;

	@JsonCreator
	public TagInput(@JsonProperty("name") String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
