package com.shirobokov.creditpipelineusers.repository;


import com.shirobokov.creditpipelineusers.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {

    Passport findById(int passportId);

}
