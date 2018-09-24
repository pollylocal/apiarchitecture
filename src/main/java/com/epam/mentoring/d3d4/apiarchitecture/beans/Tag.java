package com.epam.mentoring.d3d4.apiarchitecture.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * @author Aliaksandr Makavetski
 */
@Entity
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@ManyToMany(mappedBy = "tags")
	private List<Podcast> podcasts;

	public Tag() {
	}

	public Tag(TagInput tagInput) {
		this.name = tagInput.getName();
	}


	@JsonIgnore
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public List<Podcast> getPodcasts() {
		return podcasts;
	}

	public void setPodcasts(List<Podcast> podcasts) {
		this.podcasts = podcasts;
	}
}
