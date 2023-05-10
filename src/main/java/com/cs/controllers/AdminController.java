package com.cs.controllers;

import com.cs.entities.Advertisement;
import com.cs.entities.User;
import com.cs.helper.UserExcelExporter;
import com.cs.repositories.UserRepository;
import com.cs.services.AdvertisementService;
import com.cs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private UserExcelExporter userExcelExporter;

    @GetMapping("/dashboard")
    public ModelAndView adminDashboard(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("admin/adminDashboard");
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        modelAndView.addObject("user", user);
        modelAndView.addObject("title", "Dashboard");
        return modelAndView;
    }

    //To get the all the user
    @GetMapping("/getAllUsers")
    public ModelAndView showAllUser(Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        ModelAndView modelAndView = new ModelAndView("admin/ShowAllUser");
        List<User> allUsers = userService.getAllUsers();
        modelAndView.addObject("user", user);
        modelAndView.addObject("allUsers", allUsers);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteUser(@PathVariable("id") Integer id, RedirectView redirectView) {
        userRepository.deleteById(id);
        redirectView.setUrl("/admin/getAllUsers");
        return redirectView;
    }

    //To get the all advertisements
    @GetMapping("/getAllAdvertisements")
    public ModelAndView showAllAdvertisements(Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        ModelAndView modelAndView = new ModelAndView("admin/ShowAllAdvertisements");
        List<Advertisement> allAdvertisements = advertisementService.getAllAdvertisements();
        modelAndView.addObject("user", user);
        modelAndView.addObject("allAdvertisements", allAdvertisements);
        return modelAndView;
    }

    @GetMapping("/deleteAdvertisement/{id}")
    public RedirectView deleteAdvertisement(@PathVariable("id") Integer id, RedirectView redirectView) {
        advertisementService.deleteAdvertisement(id);
        redirectView.setUrl("/admin/getAllAdvertisements");
        return redirectView;
    }
    @GetMapping("/download/{id}")
    public ModelAndView downloadUserDetails(@PathVariable("id") int id, HttpServletResponse response) throws IOException {
        ModelAndView modelAndView = new ModelAndView("admin/ShowAllUser");
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue="attachment; filename=users.xlsx";
        response.setHeader(headerKey,headerValue);
        Optional<User> byId = userRepository.findById(id);
        if(byId.isPresent()){
            User user = byId.get();
            userExcelExporter.setUser(user);
            userExcelExporter.export(response);
            return modelAndView;
        }
        return modelAndView;
    }
}
