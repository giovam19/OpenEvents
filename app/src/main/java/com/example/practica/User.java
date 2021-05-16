package com.example.practica;

public class User {
    private static  User user;

    private int ID;
    private String userName;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String image;
    private String token;

    private User(int id, String name, String lastname, String email, String password, String image, String token) {
        this.ID = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.image = image;
        this.token = token;
        this.userName = (this.name + " " + this.lastname);
    }

    public static User set(int id, String name, String lastname, String email, String password, String image, String token) {
        user = new User(id, name, lastname, email, password, image, token);
        return user;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void actUserName() {
        this.userName = this.name + " " + this.lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
