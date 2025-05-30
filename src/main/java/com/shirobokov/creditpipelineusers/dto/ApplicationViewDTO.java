package com.shirobokov.creditpipelineusers.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApplicationViewDTO {

    private int id;

    private Integer amount;

    private Integer period;

    private String purpose;

    private String status;

    private String reasonForRefusal;

    private LocalDateTime dateOfCreation;

}
