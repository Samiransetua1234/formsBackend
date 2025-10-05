package com.samiransetua.forms.service.interfaces;

import com.samiransetua.forms.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    Optional<User> findByEmail(String email);
}
