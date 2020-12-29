package com.github.murn.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.time.LocalDateTime;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final Post post = new Post();
        post.setId(1);
        post.setAuthor("muRn");
        post.setEditor("moderator");
        post.setHeader("Top 3 realistic science-fiction movies");
        post.setContent("1. Gattaca (1997)  2. Contact (1997)  3. Metropolis (1927)");
        post.setTimestamp(LocalDateTime.parse("2021-01-04T12:34:56"));

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/post.json"), Post.class));

        assertThat(MAPPER.writeValueAsString(post)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws JsonProcessingException {
        final Post post = new Post();
        post.setId(1);
        post.setAuthor("muRn");
        post.setEditor("moderator");
        post.setHeader("Top 3 realistic science-fiction movies");
        post.setContent("1. Gattaca (1997)  2. Contact (1997)  3. Metropolis (1927)");
        post.setTimestamp(LocalDateTime.parse("2021-01-04T12:34:56"));

        assertThat(MAPPER.readValue(fixture("fixtures/post.json"), Post.class))
                .isEqualTo(post);
    }
}
