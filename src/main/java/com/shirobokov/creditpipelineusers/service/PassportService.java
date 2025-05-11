package com.shirobokov.creditpipelineusers.service;


import com.shirobokov.creditpipelineusers.entity.Passport;
import com.shirobokov.creditpipelineusers.repository.PassportRepository;
import org.springframework.stereotype.Service;

@Service
public class PassportService {


    private final PassportRepository passportRepository;

    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    public Passport findPassportById(int passportId) {
        return passportRepository.findById(passportId);
    }

    public Passport updatePassport(Passport oldPassport, Passport newPassport) {
        oldPassport.setSeries(newPassport.getSeries());
        oldPassport.setNumber(newPassport.getNumber());
        oldPassport.setBirthDate(newPassport.getBirthDate());
        oldPassport.setBirthPlace(newPassport.getBirthPlace());
        oldPassport.setDepartmentCode(newPassport.getDepartmentCode());
        oldPassport.setIssuedBy(newPassport.getIssuedBy());
        oldPassport.setIssueDate(newPassport.getIssueDate());
        oldPassport.setInn(newPassport.getInn());
        oldPassport.setSnils(newPassport.getSnils());

        return passportRepository.save(oldPassport);
    }
}
