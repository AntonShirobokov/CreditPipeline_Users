package com.shirobokov.creditpipelineusers.repository;


import com.shirobokov.creditpipelineusers.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

}
