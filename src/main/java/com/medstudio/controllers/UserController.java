package com.medstudio.controllers;

/**
 * Created by Savek on 2016-12-17.
 */

import com.medstudio.models.crud.UserRepository;
import com.medstudio.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    UserRepository repo;

    @RequestMapping("/user")
    @ResponseBody
    public User user() {
        AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        String login = auth == null ?null:((UserDetails) auth.getPrincipal()).getUsername();

        if (login != null) {
            return repo.findByLogin(login);
        }
        return null;
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public User updateUser(@RequestBody User user) {

        System.out.println(user.toString());
        repo.save(user);

        return user;
    }
}

