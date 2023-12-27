package com.city.cw.stockadvisor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Provide custom error handling logic or return the path to your error page
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}
