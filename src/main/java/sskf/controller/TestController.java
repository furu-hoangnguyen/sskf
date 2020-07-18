package sskf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.EmailModel;
import sskf.service.EmailServices;

import java.util.HashSet;
import java.util.Set;

@RestController
public class TestController {
    @Autowired
    private EmailServices emailServices;

    @GetMapping("/api/test")
    public void test() {
        EmailModel mail = new EmailModel();
        Set<String> addresses = new HashSet<>();
        addresses.add("thanh.tran@furucrm.com");

        mail.setEmailAddresses(addresses);
        mail.setSubject("Testing smtp mail");
        mail.setTextBody("Testing smtp mail body");

        emailServices.sendEmail(mail);

    }
}
