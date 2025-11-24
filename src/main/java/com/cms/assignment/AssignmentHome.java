package com.cms.assignment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentHome {

    @GetMapping("/")
    public String home() {
        return "CMS Backend is running!";
    }
}
