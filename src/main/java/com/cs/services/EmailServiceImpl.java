package com.cs.services;

import com.cs.entities.ConfirmationTokenEntity;
import com.cs.entities.User;
import com.cs.repositories.ConfirmationTokenRepository;
import com.cs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Async
    public void sendEmail(User user) {
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(user);
        confirmationTokenRepository.save(confirmationToken);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Please verify the email");
        simpleMailMessage.setText("To confirm your account, please click here : " + "http://localhost:9292/confirm-account?token=" + confirmationToken.getConfirmationToken());
        javaMailSender.send(simpleMailMessage);
    }

    public boolean confirmUser(String confirmationToken) {
        Optional<ConfirmationTokenEntity> byConfirmationToken = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (byConfirmationToken.isPresent()) {
            Optional<User> user = userService.findUser(byConfirmationToken.get().getUser().getEmail());
            user.get().setEnabled(true);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}