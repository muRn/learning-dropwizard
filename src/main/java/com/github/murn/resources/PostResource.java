package com.github.murn.resources;

import com.github.murn.api.Post;
import com.github.murn.db.JdbiPostDao;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Path("/posts/{id}")
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
    private final JdbiPostDao dao;
    private final AtomicInteger counter;

    public PostResource(JdbiPostDao dao) {
        this.dao = dao;
        this.counter = new AtomicInteger();
    }

    @GET
    public Post getPost(@PathParam("id") OptionalInt idOpt) {
        int id = validatePostId(idOpt);
        return dao.findPostById(id)
                .orElseThrow(() -> new WebApplicationException("post with id " + id + " is not present", Response.Status.NOT_FOUND));
    }

    @PUT
    public void updatePost(@PathParam("id") OptionalInt idOpt, Post post) {
        int id = validatePostId(idOpt);
        if (post.getId() != null && post.getId() != id) {
            throw new WebApplicationException("Id from path doesn't match id from body");
        }

        int rowsUpdated = dao.updatePost(post);
        if (rowsUpdated != 1) {
            throw new WebApplicationException("post with id " + id + " is not present", Response.Status.NOT_FOUND);
        }
    }

    @DELETE
    public void deletePost(@PathParam("id") OptionalInt idOpt) {
        int id = validatePostId(idOpt);
        int rowsDeleted = dao.deletePostById(id);
        if (rowsDeleted != 1) {
            throw new WebApplicationException("post with id " + id + " is not present", Response.Status.NOT_FOUND);
        }
    }

    private int validatePostId(OptionalInt idOpt) {
        if (!idOpt.isPresent()) {
            log.warn("id is not passed or malformed");
            throw new WebApplicationException("id is not passed or malformed", Response.Status.BAD_REQUEST);
        }

        return idOpt.getAsInt();
    }
}
