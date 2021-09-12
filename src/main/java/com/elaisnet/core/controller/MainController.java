package com.elaisnet.core.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping({"/", "/login"})
	public String redIndex() {
		return "redirect:/index";
	}
	
	@RequestMapping("/index")
	public String login() {
		return "login";
	}
}
