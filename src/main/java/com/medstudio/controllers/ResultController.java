package com.medstudio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Savek on 2016-12-21.
 */

@Controller
public class ResultController {

    @RequestMapping("/getResults")
    @ResponseBody
    public String create(String email, String name) {

        return "";
    }
}
