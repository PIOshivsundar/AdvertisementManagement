package com.cs.scheduler;


import com.cs.entities.Advertisement;
import com.cs.entities.User;
import com.cs.repositories.AdvertisementRepository;
import com.cs.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ShowRunningAdvertisementToUser {
    public static final Logger logger = LoggerFactory.getLogger(ShowRunningAdvertisementToUser.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    //    @Scheduled(initialDelay = 1000,fixedDelay = 40000)
    @Scheduled(cron = "0 0 10 * * SUN") // 10AM
    public void showAdvertisementToUser() throws MessagingException, UnsupportedEncodingException {
        Iterable<User> userList = userRepository.findAll();
        for (User user : userList) {
            if (!user.getRole().equals("ROLE_ADMIN")) {
                String email = user.getEmail();
                Optional<List<Advertisement>> advertisementList = advertisementRepository.findByUser(user);
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);
                helper.setFrom("admsapplication@gmail.com", "AMS App");
                helper.setTo(email);
                helper.setSubject("Advertisement Status");
                if (advertisementList.isPresent()) {
                    String content = "<p>Hello, </p>" + user.getName()
                            + "<p></p>"
                            + advertisementList.get().size() + " advertisements has been running currently... ";
                    helper.setText(content, true);
                    javaMailSender.send(message);
                } else {
                    String content = "<p>Hello, </p>"
                            + "<p>You don't have any advertisements running currently, please advertisement!  </p>";
                    helper.setText(content, true);
                    javaMailSender.send(message);
                }
                logger.info("Email has been sent to user: {}", user.getEmail());
                logger.info("Date: {}", new Date());
            }
        }
    }
}
