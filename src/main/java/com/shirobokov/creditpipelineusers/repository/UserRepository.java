package com.shirobokov.creditpipelineusers.repository;


import com.shirobokov.creditpipelineusers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPhone(String phone);

    User findById(int id);
}
