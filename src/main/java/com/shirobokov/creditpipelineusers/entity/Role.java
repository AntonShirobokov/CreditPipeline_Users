package com.shirobokov.creditpipelineusers.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="t_user_authority")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="c_authority")
    private String role;

    @OneToOne()
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

}
