package com.shirobokov.creditpipelineusers.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name="t_employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="c_username")
    private String username;

    @Column(name="c_lastname")
    private String lastName;

    @Column(name="c_firstname")
    private String firstName;

    @Column(name="c_middlename")
    private String middleName;

    @Column(name="c_password")
    private String password;

    @Column(name="c_role")
    private String role;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;
}
