package com.example.ReviewEngine.dto;


import java.util.ArrayList;
import java.util.List;

public class ProductRequest {

    private String name;
    private String category;
    private List<String> tags = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


}
