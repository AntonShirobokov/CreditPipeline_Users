package com.shirobokov.creditpipelineusers.entity;

import com.shirobokov.creditpipelineusers.entity.enums.ApplicationEnums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_employee")
    private Employee employee;

    @Column(name = "c_amount")
    private Integer amount;

    @Column(name = "c_period")
    private Integer period;

    @Column(name = "c_purpose")
    private String purpose;

    @Column(name="c_age")
    private Integer age;

    @Column(name = "c_type_of_employment")
    private String typeOfEmployment;

    @Column(name = "c_deposit")
    private String deposit;

    @Column(name = "c_income")
    private Integer income;

    @Column(name = "c_dept")
    private Integer dept;

    @Column(name = "c_education")
    private String education;

    @Column(name = "c_type_of_housing")
    private String typeOfHousing;

    @Column(name = "c_number_of_dependents")
    private Integer numberOfDependents;

    @Column(name = "c_marital_status")
    private String maritalStatus;

    @Column(name = "c_status")
    private String status;

    @Column(name = "c_reason_for_refusal")
    private String reasonForRefusal;

    @Column(name="c_was_bankrupt")
    private Boolean wasBankrupt;

    @Column(name="c_real_income")
    private Integer realIncome;

    @Column(name="c_date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name="c_score")
    private Float score;

    @Column(name="c_percentage_rate")
    private Float percentageRate;

    @Column(name="c_date_of_review")
    private LocalDateTime dateOfReview;

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", amount=" + amount +
                ", period=" + period +
                ", purpose=" + purpose +
                ", typeOfEmployment=" + typeOfEmployment +
                ", deposit=" + deposit +
                ", income=" + income +
                ", dept=" + dept +
                ", education=" + education +
                ", typeOfHousing=" + typeOfHousing +
                ", numberOfDependents=" + numberOfDependents +
                ", maritalStatus=" + maritalStatus +
                ", status=" + status +
                ", reasonForRefusal=" + reasonForRefusal +
                '}';
    }
}
