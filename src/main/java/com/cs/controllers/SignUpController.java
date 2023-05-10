package com.cs.controllers;

import com.cs.entities.User;
import com.cs.helper.Message;
import com.cs.services.EmailService;
import com.cs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class SignUpController {
    private static final String SIGNUP = "signup.html";
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //To handle data request from signup form
    @PostMapping("/do-signup")
    public ModelAndView doSignup(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(name = "agreement", defaultValue = "false") boolean agreement, ModelAndView modelAndView, HttpSession session) {
        try {
            if (!agreement) {
                throw new Exception("you have not agreed terms and conditions");
            }

            //To check all the errors in the fields
            if (result.hasErrors()) {
                modelAndView.addObject("user", user);
                modelAndView.setViewName(SIGNUP);
                return modelAndView;
            }
            //To check the user is already registered with this email
            if (userService.existsUserByEmail(user.getEmail())) {
                result.addError(new FieldError("user", "email", "an account is already registered with this email address. try with another one!"));
                if (result.hasErrors()) {
                    modelAndView.addObject("user", user);
                    modelAndView.setViewName(SIGNUP);
                    return modelAndView;
                }
            }

            //To check whether the aadhaar is already registered or not.
            if (userService.existsUserByAadhaar(user.getAadhaar())) {
                result.addError(new FieldError("user", "aadhaar", "this aadhaar number is already registered!"));
                if (result.hasErrors()) {
                    modelAndView.addObject("user", user);
                    modelAndView.setViewName(SIGNUP);
                    return modelAndView;
                }
            }
            user.setRole("ROLE_NORMAL");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addUser(user);
            //To send the verification token
            emailService.sendEmail(user);
            modelAndView.addObject("user", new User());
            session.setAttribute("message", new Message("successfully registered!!", "alert-success"));
            modelAndView.setViewName(SIGNUP);
            return modelAndView;
        } catch (Exception e) {
            modelAndView.addObject("user", user);
            session.setAttribute("message", new Message("something went wrong!    " + e.getMessage(), "alert-danger"));
            modelAndView.setViewName(SIGNUP);
            return modelAndView;
        }
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {
        if (emailService.confirmUser(confirmationToken)) {
            modelAndView.addObject("success", "your email has been verified. you can now login");
            modelAndView.setViewName("accountVerified.html");
            return modelAndView;
        }
        modelAndView.addObject("notSuccess", "your email is not verified");
        modelAndView.setViewName("accountNotVerified.html");
        return modelAndView;
    }
}
