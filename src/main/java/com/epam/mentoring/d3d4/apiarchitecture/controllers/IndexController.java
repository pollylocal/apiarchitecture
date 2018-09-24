package com.epam.mentoring.d3d4.apiarchitecture.controllers;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author Aliaksandr Makavetski
 */
@RestController
@RequestMapping("/")
public class IndexController {

	@GetMapping
	public ResourceSupport index() {
		ResourceSupport index = new ResourceSupport();
		index.add(linkTo(PodcastController.class).withRel("podcasts"));
		index.add(linkTo(TagsController.class).withRel("tags"));
		return index;
	}

}
