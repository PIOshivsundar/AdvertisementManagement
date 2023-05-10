package com.cs.controllers;

import com.cs.entities.User;
import com.cs.exception.UserNotFoundException;
import com.cs.helper.Utility;
import com.cs.services.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    //To display to forgot password form
    @GetMapping("/forgot-password")
    public ModelAndView showForgotPasswordForm(ModelAndView modelAndView) {
        modelAndView.addObject("title", "Forgot-Password");
        modelAndView.setViewName("forgot_password_form");
        return modelAndView;
    }

    //To process the forgot password form
    @PostMapping("/process-forgot-password")
    public ModelAndView processForgotPasswordForm(@RequestParam("email") String email, ModelAndView modelAndView, HttpServletRequest request) {
        String token = RandomString.make(45);
        try {
            userService.updateResetPasswordToken(token, email);
            // generate reset password link
            //send email
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            modelAndView.addObject("message", "we have sent a reset password link to your email. Please check. ");

        } catch (UserNotFoundException | MessagingException | UnsupportedEncodingException e) {
            modelAndView.addObject("error", e.getMessage());
        }
        modelAndView.setViewName("forgot_password_form");
        return modelAndView;
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("admsapplication@gmail.com", "AMS App");
        helper.setTo(email);
        helper.setSubject("Here's the link to reset the password");
        String content = "<p>Hello, </p>"
                + "<p>you have requested to reset the password. </p>"
                + "<p>Click th link below to change the password. </p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\" >Change my password</a></b> </p>"
                + "<p>Ignore this email if you do remember you password, or you have not made the request </p> ";
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    //To display to reset password form
    @GetMapping("/reset_password")
    public ModelAndView showResetPasswordForm(@Param(value = "token") String token, ModelAndView modelAndView) {
        User user = userService.get(token);
        if (user == null) {
            modelAndView.addObject("title", "Rest your password");
            modelAndView.addObject("message", "Invalid Token");
            modelAndView.setViewName("invalid_token");
            return modelAndView;
        }
        modelAndView.addObject("token", token);
        modelAndView.setViewName("reset_password_form");
        return modelAndView;
    }

    //To process the reset password form
    @PostMapping("/process-reset-password")
    public ModelAndView processResetPassword(HttpServletRequest request, ModelAndView modelAndView) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!Objects.equals(confirmPassword, password)) {
            modelAndView.addObject("error", "password is not matching!");
            modelAndView.setViewName("reset_password_form");
            return modelAndView;
        }
        User user = userService.get(token);
        if (user == null) {
            modelAndView.addObject("title", "Reset your password");
            modelAndView.addObject("message", "Invalid Token");
            modelAndView.setViewName("invalid_token");
            return modelAndView;
        }
        userService.updatePassword(user, password);
        modelAndView.addObject("message", "Password reset successfully!");
        modelAndView.setViewName("reset_password_form");
        return modelAndView;
    }
}
