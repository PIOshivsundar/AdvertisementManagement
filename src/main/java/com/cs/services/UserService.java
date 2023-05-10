package com.cs.services;

import com.cs.entities.User;
import com.cs.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void addUser(User user);
    boolean existsUserByEmail(String email);
    boolean existsUserByAadhaar(String aadhaar);
    Optional<User> findUser(String email);
    List<User> getAllUsers();
    void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
    User get(String resetPasswordToken);

    void updatePassword(User user,String newPassword);
}
