package com.github.murn.resources;

import com.github.murn.api.Post;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

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
    private final Jdbi jdbi;
    private final AtomicInteger counter;

    public PostResource(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.counter = new AtomicInteger();
    }

    @GET
    public Post getPost(@PathParam("id") OptionalInt idOpt) {
        int id = validatePostId(idOpt);
        return jdbi.withHandle(handle ->
                handle.createQuery("select * from public.posts where id=?")
                .bind(0, id)
                .mapToBean(Post.class)
                .one());
    }

    private int validatePostId(OptionalInt idOpt) {
        if (!idOpt.isPresent()) {
            log.warn("id is not passed or malformed");
            throw new WebApplicationException("id is not passed or malformed", Response.Status.BAD_REQUEST);
        }

        int id = idOpt.getAsInt();
        Optional<Post> postOpt = jdbi.withHandle(handle ->
                handle.createQuery("select * from public.posts where id=?")
                        .bind(0, id)
                        .mapToBean(Post.class)
                        .findFirst());
        if (!postOpt.isPresent()) {
            throw new WebApplicationException("id " + id + " is not present", Response.Status.NOT_FOUND);
        }

        return id;
    }

    @POST
    public Response addPost(Post post) {
        int id = counter.getAndIncrement();
        post.setId(id);
        int rowsInserted = jdbi.withHandle(handle ->
                handle.createUpdate("insert into public.posts(id, header, content, author, editor, updated_at) values(:id, :header, :content, :author, :editor, :timestamp)")
                .bindBean(post)
                .execute());
        if (rowsInserted != 1) {
            throw new WebApplicationException("Expected to insert 1 row but inserted " + rowsInserted);
        }

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

        int rowsUpdated = jdbi.withHandle(handle ->
                handle.createUpdate("update public.posts set header=?, content=?, editor=? where id=?")
                        .bind(0, post.getHeader())
                        .bind(1, post.getContent())
                        .bind(2, "moderator")
                        .bind(3, id)
                        .execute());
        if (rowsUpdated != 1) {
            throw new WebApplicationException("Expected to update 1 row but updated " + rowsUpdated);
        }
    }

    @DELETE
    public void deletePost(@PathParam("id") OptionalInt idOpt) {
        int id = validatePostId(idOpt);
        int rowsDeleted = jdbi.withHandle(handle ->
                handle.createUpdate("delete from public.posts where id=?")
                .bind(0, id)
                .execute());
        if (rowsDeleted != 1) {
            throw new WebApplicationException("Expected to delete 1 row but deleted " + rowsDeleted);
        }
    }
}
