package com.github.murn.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.time.LocalDateTime;

public class Post {
    private Integer id;
    private String author;
    private String editor;
    private String header;
    private String content;
    private LocalDateTime timestamp;

    @JsonProperty
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty
    public String getAuthor() {
        return author;
    }

    @JsonProperty
    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty
    public String getEditor() {
        return editor;
    }

    @JsonProperty
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @JsonProperty
    public String getHeader() {
        return header;
    }

    @JsonProperty
    public void setHeader(String header) {
        this.header = header;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }

    @JsonProperty
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equal(id, post.id) &&
                Objects.equal(author, post.author) &&
                Objects.equal(editor, post.editor) &&
                Objects.equal(header, post.header) &&
                Objects.equal(content, post.content) &&
                Objects.equal(timestamp, post.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, author, editor, header, content, timestamp);
    }
}
