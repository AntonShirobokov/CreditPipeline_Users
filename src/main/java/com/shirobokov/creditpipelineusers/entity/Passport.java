package com.shirobokov.creditpipelineusers.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "t_passport")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="c_series")
    private String series;

    @Column(name="c_number")
    private String number;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Column(name="c_birth_date")
    private LocalDate birthDate;

    @Column(name="c_birth_place")
    private String birthPlace;

    @Column(name="c_department_code")
    private String departmentCode;

    @Column(name="c_issued_by")
    private String issuedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Column(name="c_issue_date")
    private LocalDate issueDate;

    @Column(name="с_inn")
    private String inn;

    @Column(name="c_snils")
    private String snils;

    @Column(name="c_registration_address")
    private String registrationAddress;

    @OneToOne
    @JoinColumn(name="id_user", referencedColumnName = "id")
    private User user;


    @Override
    public String toString() {
        return "Passport{" +
                "id=" + id +
                ", series='" + series + '\'' +
                ", number='" + number + '\'' +
                ", birthDate=" + birthDate +
                ", birthPlace='" + birthPlace + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", issuedBy='" + issuedBy + '\'' +
                ", issueDate=" + issueDate +
                ", inn='" + inn + '\'' +
                ", snils='" + snils + '\'' +
                ", registrationAddress='" + registrationAddress + '\'' +
                '}';
    }
}

