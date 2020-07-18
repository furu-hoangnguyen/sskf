package sskf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sskf.service.RequestService;

@Controller
public class MainController implements ErrorController {

    @Autowired
    private RequestService requestService;

    @Override
    public String getErrorPath() {

        return "/error";
    }

    @GetMapping({ "/", "/request/*", "/master/*", "/user/*", "/login", "/account-receivables/**", "/exhibition-promotions/**", "/mannequin-promotions/**", "/error" })
    public String goToVueJsApp(Model model) {

        return "index";
    }

    @GetMapping("/request-details")
    public String getRequestDetails(@RequestParam(value = "request-cd") Long requestCd) {
        return "redirect:/" + requestService.getRequestDetails(requestCd);
    }

}
