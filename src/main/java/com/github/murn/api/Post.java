package com.github.murn.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
