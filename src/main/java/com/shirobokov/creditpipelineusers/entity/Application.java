package com.shirobokov.creditpipelineusers.entity;

import com.shirobokov.creditpipelineusers.entity.enums.ApplicationEnums.*;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "c_amount")
    private Integer amount;

    @Column(name = "c_period")
    private Integer period;

    //@Enumerated(EnumType.STRING)
    @Column(name = "c_purpose")
    private String purpose;

    //@Enumerated(EnumType.STRING)
    @Column(name = "c_type_of_employment")
    private String typeOfEmployment;

    //@Enumerated(EnumType.STRING)
    @Column(name = "c_deposit")
    private String deposit;

    @Column(name = "c_income")
    private Integer income;

    @Column(name = "c_dept")
    private Integer dept;

    //@Enumerated(EnumType.STRING)
    @Column(name = "c_education")
    private String education;

    //@Enumerated(EnumType.STRING)
    @Column(name = "c_type_of_housing")
    private String typeOfHousing;

    @Column(name = "c_number_of_dependents")
    private Integer numberOfDependents;

    //@Enumerated(EnumType.STRING)
    @Column(name = "c_marital_status")
    private String maritalStatus;

    //@Enumerated(EnumType.STRING)
    @Column(name = "c_status")
    private String status;

    @Column(name = "c_reason_for_refusal")
    private String reasonForRefusal;

    @Column(name="c_was_bankrupt")
    private Boolean wasBankrupt;

    @Column(name="c_real_income")
    private Integer realIncome;

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
