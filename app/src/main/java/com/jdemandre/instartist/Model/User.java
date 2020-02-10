package com.jdemandre.instartist.Model;

import java.util.List;

public class User {
    private String id;
    private String userName;
    private String description;
    private List<Publication> publications;
    private List<String> interests;
    private String profilePic;
    private String email;
    private String phone;
    private float earnings;
    private List<User> following;

    public User(String id, String userName, String description, List<Publication> publications, List<String> interests, String profilePic, String email, String phone, float earnings, List<User> following) {
        this.id = id;
        this.userName = userName;
        this.description = description;
        this.publications = publications;
        this.interests = interests;
        this.profilePic = profilePic;
        this.email = email;
        this.phone = phone;
        this.earnings = earnings;
        this.following = following;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getInterests() {
        return interests;
    }
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public float getEarnings() {
        return earnings;
    }
    public void setEarnings(float earnings) {
        this.earnings = earnings;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<User> getFollowing() {
        return following;
    }
    public void setFollowing(List<User> following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", description='" + description + '\'' +
                ", publications=" + publications +
                ", interests=" + interests +
                ", profilePic='" + profilePic + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", earnings=" + earnings +
                ", following=" + following +
                '}';
    }
}
