package com.github.murn.resources;

import com.github.murn.api.Post;
import com.github.murn.db.JdbiPostDao;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Path("/posts/{id}")
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
    private final Jdbi jdbi;
    private final AtomicInteger counter;

    public PostResource(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.counter = new AtomicInteger();
    }

    @GET
    public Post getPost(@PathParam("id") OptionalInt idOpt) {
        int id = validatePostId(idOpt);
        return jdbi.withExtension(JdbiPostDao.class, dao ->
                dao.getPostById(id));
    }

    @PUT
    public void updatePost(@PathParam("id") OptionalInt idOpt, Post post) {
        int id = validatePostId(idOpt);
        if (post.getId() != null && post.getId() != id) {
            throw new WebApplicationException("Id from path doesn't match id from body");
        }

        int rowsUpdated = jdbi.withExtension(JdbiPostDao.class, dao ->
                dao.updatePost(post));
        if (rowsUpdated != 1) {
            throw new WebApplicationException("Expected to update 1 row but updated " + rowsUpdated);
        }
    }

    @DELETE
    public void deletePost(@PathParam("id") OptionalInt idOpt) {
        int id = validatePostId(idOpt);
        int rowsDeleted = jdbi.withExtension(JdbiPostDao.class, dao ->
                dao.deletePostById(id));
        if (rowsDeleted != 1) {
            throw new WebApplicationException("Expected to delete 1 row but deleted " + rowsDeleted);
        }
    }

    private int validatePostId(OptionalInt idOpt) {
        if (!idOpt.isPresent()) {
            log.warn("id is not passed or malformed");
            throw new WebApplicationException("id is not passed or malformed", Response.Status.BAD_REQUEST);
        }

        int id = idOpt.getAsInt();
        Optional<Post> postOpt = jdbi.withExtension(JdbiPostDao.class, dao ->
                dao.findPostById(id));
        if (!postOpt.isPresent()) {
            throw new WebApplicationException("id " + id + " is not present", Response.Status.NOT_FOUND);
        }

        return id;
    }
}
