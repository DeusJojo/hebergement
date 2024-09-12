package com.afpa.hebergement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomePageController {


    @RequestMapping("/")
    public String root() {
        return "Bienvenue dans l'application AFPA HEBERGEMENT!!";
    }


}
