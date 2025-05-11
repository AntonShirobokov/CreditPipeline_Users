package com.shirobokov.creditpipelineusers.service;


import com.shirobokov.creditpipelineusers.entity.Passport;
import com.shirobokov.creditpipelineusers.entity.Role;
import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User findByPhone(String username) {
        return userRepository.findByPhone(username);
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }


    public User registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = new Role();
        role.setUser(user);
        role.setRole("ROLE_USER");

        user.setRole(role);

        Passport passport = new Passport();
        passport.setUser(user);

        user.setPassport(passport);

        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void updateUser(User currentUser, User updatedUser){

        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setMiddleName(updatedUser.getMiddleName());
        currentUser.setPhone(updatedUser.getPhone());
        currentUser.setEmail(updatedUser.getEmail());

        userRepository.save(currentUser);

    }
}
