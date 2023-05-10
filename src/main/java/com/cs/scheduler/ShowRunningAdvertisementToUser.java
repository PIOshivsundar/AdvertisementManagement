package com.cs.scheduler;


import com.cs.entities.User;
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

@Component
public class ShowRunningAdvertisementToUser {
    public static final Logger logger = LoggerFactory.getLogger(ShowRunningAdvertisementToUser.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    //Send email to all the users that how many advertisement currently is running...
//    @Scheduled(cron = "0/15 * * * * *")
    //@Scheduled(cron = "0 0 10 * * SUN") // 10AM
    public void showAdvertisementToUser() throws MessagingException, UnsupportedEncodingException {
        Iterable<User> userList = userRepository.findAll();
        for(User user: userList){
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("admsapplication@gmail.com", "AMS App");
            helper.setTo(user.getEmail());
            helper.setSubject("Advertisement Status");
            String content = "<p>Hello, </p>"
                    +"<p>"+(long) user.getAdvertisements().size()+"+ advertisement is currently is running...</p>";
            helper.setText(content, true);
            javaMailSender.send(message);

            logger.info("Email: {}", new Date());
        }
        logger.info("Email has been sent to user: {}", new Date());
    }
}
