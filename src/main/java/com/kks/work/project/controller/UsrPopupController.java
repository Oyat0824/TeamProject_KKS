package com.kks.work.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UsrPopupController {
	@RequestMapping("/usr/popup/jusoPopup")
	public String showPopup() {
		return "usr/popup/jusoPopup";
	}
}
