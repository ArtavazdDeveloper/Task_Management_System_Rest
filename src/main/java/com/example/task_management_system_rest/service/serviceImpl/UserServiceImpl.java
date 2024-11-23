package com.example.task_management_system_rest.service.serviceImpl;

import com.example.task_management_system_rest.entity.User;
import com.example.task_management_system_rest.repository.UserRepository;
import com.example.task_management_system_rest.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUserData(User user) {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            User existingUser = byEmail.get();
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User with email " + user.getEmail() + " not found");
        }
    }
}
