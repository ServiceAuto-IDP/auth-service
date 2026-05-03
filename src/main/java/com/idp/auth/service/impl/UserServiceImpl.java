package com.idp.auth.service.impl;

import com.idp.auth.dto.UserDTO;
import com.idp.auth.entity.User;
import com.idp.auth.repository.UserRepository;
import com.idp.auth.security.JwtUtils;
import com.idp.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void register(UserDTO userDTO) {
        // Check if user already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());

        // Encode password
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        // Save it to the database
        userRepository.save(user);
    }

    @Override
    public String login(UserDTO userDTO) {
        String username = userDTO.getUsername();

        // Check if the username exists
        if (!userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username does not exist");
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Username does not exist");
        }

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new  RuntimeException("Invalid credentials");
        }

        return jwtUtils.generateToken(user.getUsername(), user.getId());
    }
}
