package com.pennant.prodmtr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MailController {

	@RequestMapping(value = "/forgetpassword", method = RequestMethod.GET)
	public String forgotPswd(Model model) {
		System.out.println("mail forget Page");
		return "forgetpassword";
	}

}
