package com.jdemandre.instartist.Model;

public class Publication {
    private Integer id;
    private String imageUrl;
    private String description;
    private String author;

    public Publication(Integer id, String imageUrl, String description, String author) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
