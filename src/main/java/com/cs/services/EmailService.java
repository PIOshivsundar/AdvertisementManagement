package com.cs.services;

import com.cs.entities.User;

public interface EmailService {
    void sendEmail(User user);
    boolean confirmUser(String confirmationToken);
}
