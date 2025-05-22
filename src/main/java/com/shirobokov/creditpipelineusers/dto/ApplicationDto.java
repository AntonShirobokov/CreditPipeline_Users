package com.shirobokov.creditpipelineusers.dto;


import com.shirobokov.creditpipelineusers.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationDto {

    private int id;

    private Integer amount;

    private Integer period;

    private String purpose;

    private String status;

    private String reasonForRefusal;

    private String dateOfCreation;

}
