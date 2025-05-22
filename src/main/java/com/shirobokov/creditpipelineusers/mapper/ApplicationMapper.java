package com.shirobokov.creditpipelineusers.mapper;


import com.shirobokov.creditpipelineusers.dto.ApplicationDto;
import com.shirobokov.creditpipelineusers.entity.Application;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    ApplicationDto toApplicationDto(Application application);
    Application toApplication(ApplicationDto applicationDto);
}
