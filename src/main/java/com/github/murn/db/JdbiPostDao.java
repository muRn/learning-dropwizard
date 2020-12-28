package com.github.murn.db;

import com.github.murn.api.Post;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface JdbiPostDao {
    @RegisterBeanMapper(Post.class)
    @SqlQuery("select * from public.posts where id=:id")
    Post getPostById(@Bind("id") int id);

    @RegisterBeanMapper(Post.class)
    @SqlQuery("select * from public.posts where id=:id")
    Optional<Post> findPostById(@Bind("id") int id);

    @SqlUpdate("insert into public.posts(id, header, content, author, editor, updated_at) values(:id, :header, :content, :author, :editor, current_timestamp)")
    int insertNewPost(@BindBean Post post);

    @SqlUpdate("update public.posts set header=:header, content=:content, editor=:editor, updated_at=current_timestamp where id=:id")
    int updatePost(@BindBean Post post);

    @SqlUpdate("delete from public.posts where id=:id")
    int deletePostById(@Bind("id") int id);
}
