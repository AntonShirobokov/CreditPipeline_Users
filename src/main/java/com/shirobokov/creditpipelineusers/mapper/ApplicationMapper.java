package com.shirobokov.creditpipelineusers.mapper;


import com.shirobokov.creditpipelineusers.dto.ApplicationMLInputDTO;
import com.shirobokov.creditpipelineusers.dto.ApplicationViewDTO;
import com.shirobokov.creditpipelineusers.entity.Application;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    ApplicationViewDTO toApplicationViewDto(Application application);
    Application toApplication(ApplicationViewDTO applicationViewDTO);

    ApplicationMLInputDTO toApplicationMLInputDTO(Application application);
    Application toApplication(ApplicationMLInputDTO applicationMLInputDTO);
}
