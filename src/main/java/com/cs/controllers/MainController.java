package com.cs.controllers;

import com.cs.entities.Advertisement;
import com.cs.entities.User;
import com.cs.services.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MainController {

    private static final String TITLE="title";

    @Autowired
    private AdvertisementService advertisementService;

    //To get the home page
    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView){
        modelAndView.addObject(TITLE,"Home - Advertisement Management System");
        List<Advertisement> allAdvertisements = advertisementService.getAllAdvertisements();
        modelAndView.addObject("allAdvertisements",allAdvertisements);
        modelAndView.setViewName("home.html");
        return modelAndView;
    }

    //To get the about page
    @GetMapping("/about")
    public ModelAndView about(ModelAndView modelAndView){
        modelAndView.addObject(TITLE,"About - Advertisement Management System");
        modelAndView.setViewName("about.html");
        return modelAndView;
    }

    //To get the signup page
    @GetMapping("/signup")
    public ModelAndView signUp(ModelAndView modelAndView){
        modelAndView.addObject(TITLE,"SignUp - Advertisement Management System");
        modelAndView.addObject("user",new User());
        modelAndView.setViewName("signup.html");
        return modelAndView;
    }

    //To get the login page
    @GetMapping("/signIn")
    public ModelAndView login(ModelAndView modelAndView){
        modelAndView.addObject(TITLE,"Login-Advertisement Management System");
        modelAndView.setViewName("signIn.html");
        return modelAndView;
    }
}
