package com.github.murn.resources;

import com.github.murn.api.Post;
import com.github.murn.db.JdbiPostDao;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
public class PostsResource {
    private final JdbiPostDao dao;
    private final AtomicInteger counter;

    public PostsResource(JdbiPostDao dao) {
        this.dao = dao;
        this.counter = new AtomicInteger();
    }

    @GET
    public List<Post> getPosts() {
        return dao.getAllPosts();
    }

    @POST
    public Response addPost(Post post) {
        int id = counter.incrementAndGet();
        post.setId(id);
        int rowsInserted = dao.insertNewPost(post);
        if (rowsInserted != 1) {
            throw new WebApplicationException("Something went wrong when inserting new post");
        }

        return Response.created(UriBuilder.fromResource(PostsResource.class)
                .build(post.getId()))
                .build();
    }
}
