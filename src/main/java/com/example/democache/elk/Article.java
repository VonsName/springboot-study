package com.example.democache.elk;

import io.searchbox.annotations.JestId;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author ASUS
 */
@Document(indexName = "es",type = "ar")
public class Article {
    /**
     * 指定为elasticsearch 的id
     */
    @JestId
    private Integer id;
    private String name;
    private String author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
