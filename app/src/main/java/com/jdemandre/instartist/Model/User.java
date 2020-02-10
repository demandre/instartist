package com.jdemandre.instartist.Model;

import java.util.List;

public class User {
    private String userName;
    private String description;
    private List<Publication> publications;
    private String interests;
    private String ProfilePic;
    private String email;
    private Integer phone;

    public User(String userName, String description, List<Publication> publications, String interests, String profilePic, String email, Integer phone) {
        this.userName = userName;
        this.description = description;
        this.publications = publications;
        this.interests = interests;
        ProfilePic = profilePic;
        this.email = email;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<Publication> getPublications() {
        return publications;
    }
    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public String getInterests() {
        return interests;
    }
    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getProfilePic() {
        return ProfilePic;
    }
    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }
    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", description='" + description + '\'' +
                ", publications=" + publications +
                ", interests='" + interests + '\'' +
                ", ProfilePic='" + ProfilePic + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                '}';
    }
}
