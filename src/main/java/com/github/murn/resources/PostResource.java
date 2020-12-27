package com.github.murn.resources;

import com.github.murn.api.Post;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Path("/posts/{id}")
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
    private final Map<Integer, Post> allPosts;
    private final AtomicInteger counter;

    public PostResource() {
        this.allPosts = new HashMap<>();
        this.counter = new AtomicInteger();
    }

    @GET
    public Post getPost(@PathParam("id") OptionalInt idOpt) {
        int id = validatePostId(idOpt);
        return allPosts.get(id);
    }

    private int validatePostId(OptionalInt idOpt) {
        if (!idOpt.isPresent()) {
            log.warn("id is not passed or malformed");
            throw new WebApplicationException("id is not passed or malformed", Response.Status.BAD_REQUEST);
        }

        int id = idOpt.getAsInt();
        if (!allPosts.containsKey(id)) {
            throw new WebApplicationException("id " + id + " is not present", Response.Status.NOT_FOUND);
        }

        return id;
    }

    @POST
    public Response addPost(Post post) {
        int id = counter.getAndIncrement();
        allPosts.put(id, post.updateId(id));
        return Response.created(UriBuilder.fromResource(PostResource.class)
                .build(post.getId()))
                .build();
    }

    @PUT
    public void updatePost(@PathParam("id") OptionalInt idOpt, Post post) {
        int id = validatePostId(idOpt);
        if (post.getId() != null && post.getId() != id) {
            throw new WebApplicationException("Id from path doesn't match id from body");
        }

        allPosts.put(id, post);
    }

    @DELETE
    public void deletePost(@PathParam("id") OptionalInt idOpt) {
        int id = validatePostId(idOpt);
        allPosts.remove(id);
    }
}
