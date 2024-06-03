package com.example.a.controller;

import com.example.a.entity.User;
import com.example.a.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Validated
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;
    String expassword;
    private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UserController.class);

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @GetMapping("/listUser")
    public ModelAndView listUser(){
        ModelAndView modelAndView = new ModelAndView("allUser");
        List<User> users = userService.getAllUsers();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView registrationForm() {
        ModelAndView registerModelView = new ModelAndView("register");
        registerModelView.addObject("user", new User());
        return registerModelView;
    }

    //TODO: do not use Model Attribute, use RequestBody: Partially Done
    @PostMapping("/register")
    public ModelAndView registerUser(@RequestBody @Valid User user) {
//        LOGGER.info(String.valueOf(user));
        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser.getId() == null) {
            userService.addUser(user);
            return new ModelAndView("redirect:/");
        } else {
            ModelAndView registrationFailedModelView = new ModelAndView("registrationfailed");
            registrationFailedModelView.setStatus(HttpStatus.FORBIDDEN);
            return registrationFailedModelView;
        }
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editForm(@PathVariable Long id) throws NoSuchElementException {
        User user = userService.getUserById(id);
        if (user.getId() == null) {
            ModelAndView errorModelView = new ModelAndView("error");
            errorModelView.setStatus(HttpStatus.NOT_FOUND);
            return errorModelView;
        }
        expassword = user.getPassword();
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView updateUser(@ModelAttribute @Valid User user) {
        if (user.getPassword().equals(expassword)) {
            userService.updateUser(user);
            return new ModelAndView("redirect:/");
        } else {
            ModelAndView editFailedModelView = new ModelAndView("editFailed");
            editFailedModelView.setStatus(HttpStatus.FORBIDDEN);
            return editFailedModelView;
        }

    }

    //TODO: change it to post mapping: Done
    @PostMapping(path = "/delete/{id}")
    public ModelAndView deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/");
    }


    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView("error");
    }

    @GetMapping("/editFailed")
    public ModelAndView wrong() {
        return new ModelAndView("editFailed");
    }
}
