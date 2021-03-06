package org.kh.splendy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/lobby/";
        } else {
            return "index";
        }
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexWithMsg(@RequestParam("msg") String msg, Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/lobby/";
        } else {
            if(msg == null) {
                msg = "";
            }
            model.addAttribute(msg);
            return "index";
        }
    }

}
