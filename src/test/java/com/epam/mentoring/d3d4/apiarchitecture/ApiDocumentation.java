/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.mentoring.d3d4.apiarchitecture;

import com.epam.mentoring.d3d4.apiarchitecture.beans.Podcast;
import com.epam.mentoring.d3d4.apiarchitecture.beans.PodcastInput;
import com.epam.mentoring.d3d4.apiarchitecture.repositories.PodcastRepository;
import com.epam.mentoring.d3d4.apiarchitecture.repositories.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.RequestDispatcher;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiDocumentation {
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	private RestDocumentationResultHandler documentationHandler;
	@Autowired
	private PodcastRepository podcastRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;
	@Before
	public void setUp() {
		this.documentationHandler = document("{method-name}",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()));

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
			.apply(documentationConfiguration(this.restDocumentation))
			.alwaysDo(this.documentationHandler)
			.build();
	}

	@Test
	public void headersExample() throws Exception {
		this.mockMvc
			.perform(get("/"))
			.andExpect(status().isOk())
			.andDo(this.documentationHandler.document(
				responseHeaders(
					headerWithName("Content-Type").description("The Content-Type of the payload, e.g. `application/hal+json`"))));
	}

	@Test
	public void errorExample() throws Exception {
		this.mockMvc
			.perform(get("/error")
				.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
				.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/podcasts")
				.requestAttr(RequestDispatcher.ERROR_MESSAGE, "The tag 'http://localhost:8080/tags/123' does not exist"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("error", is("Bad Request")))
			.andExpect(jsonPath("timestamp", is(notNullValue())))
			.andExpect(jsonPath("status", is(400)))
			.andExpect(jsonPath("path", is(notNullValue())))
			.andDo(this.documentationHandler.document(
				responseFields(
					fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
					fieldWithPath("message").description("A description of the cause of the error"),
					fieldWithPath("path").description("The path to which the request was made"),
					fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
					fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
	}

	@Test
	public void indexExample() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andDo(this.documentationHandler.document(
				links(
					linkWithRel("podcasts").description("The <<resources-podcasts,Podcasts resource>>"),
					linkWithRel("tags").description("The <<resources-tags,Tags resource>>")),
				responseFields(
					subsectionWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));
	}

	@Test
	public void podcastsListExample() throws Exception {
		this.podcastRepository.deleteAll();

		createPodcast("REST maturity model", "http://martinfowler.com/articles/richardsonMaturityModel.html");
		createPodcast("Hypertext Application Language (HAL)", "http://stateless.co/hal_specification.html");
		createPodcast("Application-Level Profile Semantics (ALPS)", "http://alps.io/spec/");

		this.mockMvc
			.perform(get("/podcasts"))
			.andExpect(status().isOk())
			.andDo(this.documentationHandler.document(
				responseFields(
					subsectionWithPath("_embedded.podcastList").description("An array of <<resources-podcast, Podcast resources>>"))));
	}

	@Test
	public void podcastsCreateExample() throws Exception {
		Map<String, String> tag = new HashMap<>();
		tag.put("name", "REST");

		String tagLocation = this.mockMvc
			.perform(post("/tags")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(tag)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");

		Map<String, Object> podcast = new HashMap<String, Object>();
		podcast.put("title", "REST maturity model");
		podcast.put("description", "http://martinfowler.com/articles/richardsonMaturityModel.html");
		podcast.put("tags", Arrays.asList(tagLocation));

		ConstrainedFields fields = new ConstrainedFields(PodcastInput.class);

		this.mockMvc
			.perform(post("/podcasts")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(podcast)))
			.andExpect(
				status().isCreated())
			.andDo(this.documentationHandler.document(
				requestFields(
					fields.withPath("title").description("The title of the podcast"),
					fields.withPath("description").description("The body of the podcast"),
					fields.withPath("tags").description("An array of tag resource URIs"))));
	}

	@Test
	public void podcastGetExample() throws Exception {
		Map<String, String> tag = new HashMap<>();
		tag.put("name", "REST");

		String tagLocation = this.mockMvc
			.perform(post("/tags")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(tag)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");

		Map<String, Object> podcast = new HashMap<>();
		podcast.put("title", "REST maturity model");
		podcast.put("description", "http://martinfowler.com/articles/richardsonMaturityModel.html");
		podcast.put("tags", Arrays.asList(tagLocation));

		String podcastLocation = this.mockMvc
			.perform(post("/podcasts")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(podcast)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");

		this.mockMvc
			.perform(get(podcastLocation))
			.andExpect(status().isOk())
			.andExpect(jsonPath("title", is(podcast.get("title"))))
			.andExpect(jsonPath("description" , is(podcast.get("description"))))
			.andExpect(jsonPath("_links.self.href", is(podcastLocation)))
			.andExpect(jsonPath("_links.podcast-tags", is(notNullValue())))
			.andDo(this.documentationHandler.document(
				links(
					linkWithRel("self").description("This <<resources-podcast,podcast>>"),
					linkWithRel("podcast-tags").description("This podcast's <<resources-podcast-tags,tags>>")),
				responseFields(
					fieldWithPath("title").description("The title of the podcast"),
					fieldWithPath("description").description("The description of the podcast"),
					subsectionWithPath("_links").description("<<resources-podcast-links,Links>> to other resources"))));

	}

	@Test
	public void podcastUpdateExample() throws Exception {
		Map<String, Object> podcast = new HashMap<>();
		podcast.put("title", "REST maturity model");
		podcast.put("body", "http://martinfowler.com/articles/richardsonMaturityModel.html");

		String podcastLocation = this.mockMvc
			.perform(post("/podcasts")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(podcast)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");

		this.mockMvc
			.perform(get(podcastLocation))
			.andExpect(status().isOk())
			.andExpect(jsonPath("title", is(podcast.get("title"))))
			.andExpect(jsonPath("description", is(podcast.get("description"))))
			.andExpect(jsonPath("_links.self.href", is(podcastLocation)))
			.andExpect(jsonPath("_links.podcast-tags", is(notNullValue())));

		Map<String, String> tag = new HashMap<String, String>();
		tag.put("name", "REST");

		String tagLocation = this.mockMvc
			.perform(post("/tags")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(tag)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");

		Map<String, Object> podcastUpdate = new HashMap<>();
		podcastUpdate.put("tags", Collections.singletonList(tagLocation));

		ConstrainedFields fields = new ConstrainedFields(PodcastInput.class);

		this.mockMvc
			.perform(put(podcastLocation)
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(podcastUpdate)))
			.andExpect(status().isNoContent())
			.andDo(this.documentationHandler.document(
				requestFields(
					fields.withPath("title")
						.description("The title of the podcast")
						.type(JsonFieldType.STRING)
						.optional(),
					fields.withPath("description")
						.description("The description of the podcast")
						.type(JsonFieldType.STRING)
						.optional(),
					fields.withPath("tags")
						.description("An array of tag resource URIs"))));
	}

	private void createPodcast(String title, String description) {
		Podcast note = new Podcast();
		note.setTitle(title);
		note.setDescription(description);
		this.podcastRepository.save(note);
	}

	private static class ConstrainedFields {

		private final ConstraintDescriptions constraintDescriptions;

		ConstrainedFields(Class<?> input) {
			this.constraintDescriptions = new ConstraintDescriptions(input);
		}

		private FieldDescriptor withPath(String path) {
			return fieldWithPath(path).attributes(key("constraints").value(StringUtils
					.collectionToDelimitedString(this.constraintDescriptions
							.descriptionsForProperty(path), ". ")));
		}
	}

}
