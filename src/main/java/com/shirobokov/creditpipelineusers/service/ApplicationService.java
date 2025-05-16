package com.shirobokov.creditpipelineusers.service;


import com.shirobokov.creditpipelineusers.entity.Application;
import com.shirobokov.creditpipelineusers.repository.ApplicationRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }
}
