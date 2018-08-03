package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ServiceController {
	
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	public String getData(@RequestParam("input") String input) {
		System.out.println("HomeController: Passing through...");
		return "derp" + input;
	}
	
	@RequestMapping(value = "/postData", method = RequestMethod.POST)
	public String home() {
		System.out.println("HomeController: Passing through...");
		return "derp";
	}
}
