package com.idp.auth.service;

import com.idp.auth.dto.UserDTO;

public interface UserService {
    void register(UserDTO userDTO);
    String login(UserDTO userDTO);
}
