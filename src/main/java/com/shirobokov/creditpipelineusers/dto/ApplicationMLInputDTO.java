package com.shirobokov.creditpipelineusers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ApplicationMLInputDTO {

    @JsonProperty("c_amount")
    private Integer amount;

    @JsonProperty("c_period")
    private Integer period;

    @JsonProperty("c_purpose")
    private String purpose;

    @JsonProperty("c_type_of_employment")
    private String typeOfEmployment;

    @JsonProperty("c_deposit")
    private String deposit;

    @JsonProperty("c_income")
    private Integer income;

    @JsonProperty("c_dept")
    private Integer dept;

    @JsonProperty("c_education")
    private String education;

    @JsonProperty("c_type_of_housing")
    private String typeOfHousing;

    @JsonProperty("c_number_of_dependents")
    private Integer numberOfDependents;

    @JsonProperty("c_marital_status")
    private String maritalStatus;

    @JsonProperty("c_age")
    private Integer age;

    @JsonProperty("c_was_bankrupt")
    private Boolean wasBankrupt;

    @JsonProperty("c_real_income")
    private Integer realIncome;

    private Float score;

    private Float percentageRate;

}
