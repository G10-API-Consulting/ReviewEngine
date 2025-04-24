//package com.example.ReviewEngine.model;
//
//import jakarta.persistence.*;
//
//import java.util.Date;
//
//@Entity
//public class User {
//
//    public enum Role {
//        ADMIN,
//        CUSTOMER
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false, unique = true)
//    private String userName;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Temporal(TemporalType.DATE)
//    @Column(nullable = false)
//    private Date date;
//
//    @Column(nullable = false, unique = true)
//    private String apiKey;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
//
//    public User() {
//
//    }
//
//    @PrePersist //Sets the date just before saving user to database
//    protected void onCreate() {
//        date = new Date();
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getApiKey() {
//        return apiKey;
//    }
//
//    public void setApiKey(String apiKey) {
//        this.apiKey = apiKey;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//}
