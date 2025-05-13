package com.shirobokov.creditpipelineusers.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="t_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="c_phone")
    private String phone;

    @Column(name="c_password")
    private String password;

    @Column(name="c_lastname")
    private String lastName;

    @Column(name="c_firstname")
    private String firstName;

    @Column(name="c_middlename")
    private String middleName;

    @Column(name="c_email")
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Passport passport;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
