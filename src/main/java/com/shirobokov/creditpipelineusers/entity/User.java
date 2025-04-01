package com.shirobokov.creditpipelineusers.entity;


import jakarta.persistence.*;
import lombok.*;

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


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Role role;



}
