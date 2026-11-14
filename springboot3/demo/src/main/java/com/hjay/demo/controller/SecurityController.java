package com.hjay.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @PreAuthorize("hasAnyRole('admin', 'hr')")
    @GetMapping("/anyrole")
    public String hasAnyRole(){
        return "hello any role";
    }

    @PreAuthorize("hasAnyAuthority('file_read')")
    @GetMapping("/authority")
    public String hasAnyAuthority(){
        return "hello authority";
    }

    @PreAuthorize("hasRole('user')")
    @GetMapping("/role")
    public String hasRole(){
        return "hello role";
    }
}
